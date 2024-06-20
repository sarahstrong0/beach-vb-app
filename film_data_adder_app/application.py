from flask import Flask, render_template, request, redirect, url_for, send_file, flash
import xml.etree.ElementTree as ET
import os


app = Flask(__name__)
app.secret_key = 'supersecretkey'
UPLOAD_FOLDER = 'uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

points = []
current_point = 0
xml_file_path = None
original_filename = None

def load_xml(xml_path):
    global points, current_point, xml_file_path, original_filename
    xml_file_path = xml_path
    original_filename = os.path.splitext(os.path.basename(xml_file_path))[0]
    tree = ET.parse(xml_file_path)
    root = tree.getroot()
    points = []
    for instance in root.findall('.//ALL_INSTANCES/instance'):
        start_time_element = instance.find('start')
        labels = instance.findall('label')
        if start_time_element is not None and labels:
            for label in labels:
                group_element = label.find('group')
                text_element = label.find('text')
                if group_element is not None and text_element is not None:
                    group_type = group_element.text
                    text_value = text_element.text
                    if group_type == 'type' and text_value == 'serve':
                        start_time = start_time_element.text
                        player = instance.find('code').text
                        serve_start = None
                        serve_end = None
                        for lbl in instance.findall('label'):
                            grp = lbl.find('group')
                            txt = lbl.find('text')
                            if grp is not None and txt is not None:
                                if grp.text == 'serveStartZone':
                                    serve_start = txt.text
                                elif grp.text == 'serveEndZone':
                                    serve_end = txt.text
                        points.append({'startTime': float(start_time), 'player': player, 'element': instance, 'serve_start': serve_start, 'serve_end': serve_end})
                        break  # Exit the loop once we find the correct label
    points.sort(key=lambda x: x['startTime'])  # Sort points by startTime
    current_point = 0
    print(f"Loaded {len(points)} serve instances from {xml_path}")
    for point in points:
        print(f"Point: {point['startTime']}, Player: {point['player']}, Serve Start: {point['serve_start']}, Serve End: {point['serve_end']}")

def seconds_to_minutes_seconds(seconds):
    minutes = int(seconds) // 60
    remaining_seconds = int(seconds) % 60
    return f"{minutes}:{remaining_seconds:02}"

@app.route('/', methods=['GET', 'POST'])
def index():
    global current_point, points, xml_file_path, original_filename

    if request.method == 'POST':
        if 'file' in request.files:
            file = request.files['file']
            if file.filename == '':
                flash('No selected file')
                return redirect(request.url)
            if file and file.filename.endswith('.xml'):
                xml_path = os.path.join(UPLOAD_FOLDER, file.filename)
                file.save(xml_path)
                try:
                    load_xml(xml_path)
                    if len(points) == 0:
                        flash('No valid serve instances found in the uploaded XML file.')
                    return redirect(url_for('index'))
                except ET.ParseError:
                    flash('Error parsing XML file. Please ensure it is well-formed.')
                    return redirect(request.url)
            else:
                flash('Unsupported file format. Please upload an XML file.')
                return redirect(request.url)
        
        serve_start = request.form.get('serve_start')
        serve_end = request.form.get('serve_end')
        if serve_start and serve_end:
            if serve_start.isdigit() and serve_end.isdigit():
                serve_start = int(serve_start)
                serve_end = int(serve_end)
                if 1 <= serve_start <= 5 and 1 <= serve_end <= 5:
                    point = points[current_point]
                    serve_element = point['element']
                    
                    # Remove existing serve start and end labels if any
                    for label in serve_element.findall('label'):
                        group = label.find('group')
                        if group is not None and group.text in ['serveStartZone', 'serveEndZone']:
                            serve_element.remove(label)
                            print(f"Removed existing label: {group.text}")  # Debugging print statement
                    
                    # Add new serve start label
                    serve_start_label = ET.SubElement(serve_element, 'label')
                    serve_start_group = ET.SubElement(serve_start_label, 'group')
                    serve_start_group.text = 'serveStartZone'
                    serve_start_text = ET.SubElement(serve_start_label, 'text')
                    serve_start_text.text = str(serve_start)
                    print(f"Added new serve start label with value: {serve_start}")  # Debugging print statement
                    
                    # Add new serve end label
                    serve_end_label = ET.SubElement(serve_element, 'label')
                    serve_end_group = ET.SubElement(serve_end_label, 'group')
                    serve_end_group.text = 'serveEndZone'
                    serve_end_text = ET.SubElement(serve_end_label, 'text')
                    serve_end_text.text = str(serve_end)
                    print(f"Added new serve end label with value: {serve_end}")  # Debugging print statement
                    
                    point['serve_start'] = str(serve_start)
                    point['serve_end'] = str(serve_end)

                    current_point += 1
                    if current_point >= len(points):
                        current_point = len(points)
            return redirect(url_for('index'))
        elif 'previous' in request.form:
            if current_point > 0:
                current_point -= 1
            return redirect(url_for('index'))
        elif 'clear' in request.form:
            points.clear()
            current_point = 0
            xml_file_path = None
            original_filename = None
            return redirect(url_for('index'))
        elif 'clear_all' in request.form:
            points.clear()
            current_point = 0
            xml_file_path = None
            original_filename = None
            return redirect(url_for('index'))
        elif 'save' in request.form:
            return redirect(url_for('save_xml'))
    current_point_data = points[current_point] if current_point < len(points) else None
    if current_point_data:
        current_point_data['displayTime'] = seconds_to_minutes_seconds(current_point_data['startTime'])
    else:
        current_point_data = {}
    serves_left = len(points) - current_point if points else 0
    return render_template('index.html', current_point_data=current_point_data, current_point=current_point, total_points=len(points), original_filename=original_filename or "", serves_left=serves_left, str=str)
@app.route('/save_xml', methods=['GET', 'POST'])
def save_xml():
    global xml_file_path, original_filename
    if xml_file_path and original_filename:
        tree = ET.parse(xml_file_path)
        root = tree.getroot()
        
        # Apply changes directly to the tree
        for i, point in enumerate(points):
            serve_element = point['element']
            
            # Locate the instance in the tree by its position (index)
            tree_instance = root.findall('.//ALL_INSTANCES/instance')[i]

            if tree_instance is not None:
                # Remove existing serve start and end labels
                for label in tree_instance.findall('label'):
                    group = label.find('group')
                    if group is not None and group.text in ['serveStartZone', 'serveEndZone']:
                        tree_instance.remove(label)

                # Add new serve start label
                serve_start_label = ET.SubElement(tree_instance, 'label')
                serve_start_group = ET.SubElement(serve_start_label, 'group')
                serve_start_group.text = 'serveStartZone'
                serve_start_text = ET.SubElement(serve_start_label, 'text')
                serve_start_text.text = point['serve_start']

                # Add new serve end label
                serve_end_label = ET.SubElement(tree_instance, 'label')
                serve_end_group = ET.SubElement(serve_end_label, 'group')
                serve_end_group.text = 'serveEndZone'
                serve_end_text = ET.SubElement(serve_end_label, 'text')
                serve_end_text.text = point['serve_end']
            else:
                print(f"Warning: No instance found for serve element with player: {point['player']} and start time: {point['startTime']}")

        updated_filename = f"{original_filename}_SZ.xml"
        updated_path = os.path.join(UPLOAD_FOLDER, updated_filename)
        tree.write(updated_path)
        print(f"Saved updated XML to {updated_path}")

        return send_file(updated_path, as_attachment=True, attachment_filename=updated_filename)
    flash('No XML file loaded to save.')
    return redirect(url_for('index'))

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)
