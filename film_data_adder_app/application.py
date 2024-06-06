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
    tree = ET.parse(xml_path)
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
                        points.append({'startTime': start_time, 'player': player, 'element': instance})
                        break  # Exit the loop once we find the correct label
    current_point = 0
    print(f"Loaded {len(points)} serve instances from {xml_path}")

@app.route('/', methods=['GET', 'POST'])
def index():
    global current_point, points, xml_file_path

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
        elif 'serve_start' in request.form and 'serve_end' in request.form:
            serve_start = request.form['serve_start']
            serve_end = request.form['serve_end']
            if serve_start.isdigit() and serve_end.isdigit():
                serve_start = int(serve_start)
                serve_end = int(serve_end)
                if 1 <= serve_start <= 5 and 1 <= serve_end <= 5:
                    point = points[current_point]
                    serve_element = point['element']
                    serve_start_label = ET.SubElement(serve_element, 'label')
                    serve_start_group = ET.SubElement(serve_start_label, 'group')
                    serve_start_group.text = 'serveStartLocation'
                    serve_start_text = ET.SubElement(serve_start_label, 'text')
                    serve_start_text.text = str(serve_start)
                    serve_end_label = ET.SubElement(serve_element, 'label')
                    serve_end_group = ET.SubElement(serve_end_label, 'group')
                    serve_end_group.text = 'serveEndLocation'
                    serve_end_text = ET.SubElement(serve_end_label, 'text')
                    serve_end_text.text = str(serve_end)
                    current_point += 1
                    if current_point >= len(points):
                        current_point = len(points)  # Stop at the last point
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
    current_point_data = points[current_point] if current_point < len(points) else None
    return render_template('index.html', current_point_data=current_point_data, current_point=current_point, total_points=len(points))

@app.route('/save', methods=['POST'])
def save_xml():
    global xml_file_path, original_filename
    if xml_file_path and original_filename:
        tree = ET.parse(xml_file_path)
        updated_filename = f"{original_filename}_with_serve_data.xml"
        updated_path = os.path.join(UPLOAD_FOLDER, updated_filename)
        tree.write(updated_path)
        return send_file(updated_path, as_attachment=True, attachment_filename=updated_filename)
    flash('No XML file loaded to save.')
    return redirect(url_for('index'))

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)
