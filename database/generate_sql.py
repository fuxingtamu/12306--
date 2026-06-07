# -*- coding: utf-8 -*-
import os
import json
import glob
from datetime import datetime

EXTRACTED_DIR = os.path.join(os.path.dirname(__file__), '12306-data', 'utf8_extracted')
OUTPUT_FILE = os.path.join(os.path.dirname(__file__), 'import_output', 'complete_import.sql')

def get_train_type(train_code):
    code_upper = train_code.upper()
    if code_upper.startswith('G'): return 1
    elif code_upper.startswith('D'): return 2
    elif code_upper.startswith('C'): return 3
    elif code_upper.startswith('Z'): return 4
    elif code_upper.startswith('T'): return 5
    elif code_upper.startswith('K'): return 6
    else: return 7

def parse_time(time_str):
    if not time_str or time_str in ['--', '----']: return None
    try:
        parts = time_str.split(':')
        if len(parts) == 2:
            return f"{int(parts[0]):02d}:{int(parts[1]):02d}:00"
    except: pass
    return None

def escape_sql(s):
    if s is None: return ''
    return str(s).replace("'", "''")

os.makedirs(os.path.dirname(OUTPUT_FILE), exist_ok=True)

trains = []
stops = []

print("处理车次数据...")

for train_type in ['G', 'D', 'C', 'K', 'T', 'Z', 'P', 'S', 'Y']:
    json_dir = os.path.join(EXTRACTED_DIR, train_type)
    if not os.path.exists(json_dir): continue
    
    json_files = glob.glob(os.path.join(json_dir, '*.json'))
    print(f"  {train_type}: {len(json_files)} 个文件")
    
    for json_file in json_files:
        try:
            with open(json_file, 'r', encoding='utf-8') as f:
                data = json.load(f)
            
            stations_data = data.get('data', {}).get('data', [])
            if not stations_data: continue
            
            first = stations_data[0]
            train_code = first.get('station_train_code', '').strip()
            if not train_code: continue
            
            start_station = first.get('start_station_name', '')
            end_station = first.get('end_station_name', '')
            train_type_id = get_train_type(train_code)
            
            # 计算时间
            start_time = parse_time(stations_data[0].get('start_time', ''))
            end_time = parse_time(stations_data[-1].get('arrive_time', ''))
            total_time = 0
            if start_time and end_time:
                try:
                    t1 = datetime.strptime(start_time, '%H:%M:%S')
                    t2 = datetime.strptime(end_time, '%H:%M:%S')
                    total_time = int((t2 - t1).total_seconds() / 60)
                except: pass
            
            trains.append(f"('{escape_sql(train_code)}', {train_type_id}, '{escape_sql(start_station)}', '{escape_sql(end_station)}', '{start_time or ''}', '{end_time or ''}', {total_time}, 1)")
            
            for station_info in stations_data:
                station_name = station_info.get('station_name', '')
                stop_order = int(station_info.get('station_no', 0))
                arrive_time = parse_time(station_info.get('arrive_time', '')) or ''
                depart_time = parse_time(station_info.get('start_time', '')) or ''
                is_start = 1 if station_info.get('is_start') == 'Y' else 0
                is_end = 1 if station_name == end_station else 0
                
                stops.append(f"('{escape_sql(train_code)}', '{escape_sql(station_name)}', {stop_order}, '{arrive_time}', '{depart_time}', {is_start}, {is_end})")
        
        except Exception as e:
            pass

print(f"\n生成SQL文件...")
with open(OUTPUT_FILE, 'w', encoding='utf-8') as f:
    f.write("-- 12306 Complete Import SQL\n")
    f.write(f"-- Generated: {datetime.now()}\n\n")
    f.write("USE railway_system;\n\n")
    f.write("-- Modify table structure to allow NULL station_id\n")
    f.write("ALTER TABLE trains MODIFY COLUMN start_station_id BIGINT NULL;\n")
    f.write("ALTER TABLE trains MODIFY COLUMN end_station_id BIGINT NULL;\n\n")
    f.write("-- Clear old data\n")
    f.write("TRUNCATE TABLE train_stops;\n")
    f.write("TRUNCATE TABLE trains;\n\n")
    
    # Write trains
    f.write(f"-- Trains ({len(trains)} records)\n")
    f.write("INSERT INTO trains (train_code, train_type, start_station_name, end_station_name, start_time, end_time, total_time, status) VALUES\n")
    f.write(',\n'.join(trains))
    f.write(';\n\n')
    
    # Write stops
    f.write(f"-- Train Stops ({len(stops)} records)\n")
    f.write("INSERT INTO train_stops (train_code, station_name, stop_order, arrive_time, depart_time, is_start, is_end) VALUES\n")
    f.write(',\n'.join(stops))
    f.write(';\n')

print(f"完成! 文件: {OUTPUT_FILE}")
print(f"车次: {len(trains)}, 经停站: {len(stops)}")
