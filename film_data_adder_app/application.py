from flask import Flask, render_template, request, redirect, url_for
import xml.etree.ElementTree as ET
import os

app = Flask(__name__)
UPLOAD_FOLDER = 'uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

points = []
current_point = 0
xml_file_path = None

def load_xml(xml_path):
    global points, current_point, xml_file_path
    xml_file_path = xml_path
    tree = ET.parse(xml_path)
    root = tree.getroot()
    points = []
    for serve in root.findall('.//serve'):
        timestamp = serve.find('timestamp').text
        player = serve.find('player').text
        points.append({'timestamp': timestamp, 'player': player, 'element': serve})
    current_point = 0

@app.route('/', methods=['GET', 'POST'])
def index():
    global current_point, points, xml_file_path

    if request.method == 'POST':
        if 'file' in request.files:
            file = request.files['file']
            if file.filename.endswith('.xml'):
                xml_path = os.path.join(UPLOAD_FOLDER, file.filename)
                file.save(xml_path)
                load_xml(xml_path)
                return redirect(url_for('index'))
        elif 'serve_start' in request.form and 'serve_end' in request.form:
            serve_start = request.form['serve_start']
            serve_end = request.form['serve_end']
            if serve_start.isdigit() and serve_end.isdigit():
                serve_start = int(serve_start)
                serve_end = int(serve_end)
                if 1 <= serve_start <= 5 and 1 <= serve_end <= 5:
                    point = points[current_point]
                    ET.SubElement(point['element'], 'serveStartLocation').text = str(serve_start)
                    ET.SubElement(point['element'], 'serveEndLocation').text = str(serve_end)
                    current_point += 1
                    if current_point >= len(points):
                        current_point = 0
            return redirect(url_for('index'))
    current_point_data = points[current_point] if current_point < len(points) else None
    return render_template('index.html', current_point_data=current_point_data)

@app.route('/save')
def save_xml():
    global xml_file_path
    if xml_file_path:
        tree = ET.parse(xml_file_path)
        tree.write(xml_file_path)
    return redirect(url_for('index'))

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)
