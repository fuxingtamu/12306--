# -*- coding: utf-8 -*-
"""
12306新数据导入脚本
将 utf8_extracted 中的数据导出为 SQL 文件

用法: python import_12306_new.py
"""
import os
import json
import glob
from datetime import datetime

# 数据目录
DATA_DIR = os.path.dirname(os.path.abspath(__file__))
EXTRACTED_DIR = os.path.join(DATA_DIR, '12306-data', 'utf8_extracted')

# 输出目录
OUTPUT_DIR = os.path.join(DATA_DIR, 'import_output')
TRAIN_STOPS_OUTPUT = os.path.join(OUTPUT_DIR, 'train_stops_new.sql')


def get_train_type(train_code):
    """根据车次号判断类型"""
    code_upper = train_code.upper()
    if code_upper.startswith('G'):
        return 1  # 高铁
    elif code_upper.startswith('D'):
        return 2  # 动车
    elif code_upper.startswith('C'):
        return 3  # 城际
    elif code_upper.startswith('Z'):
        return 4  # 直达
    elif code_upper.startswith('T'):
        return 5  # 特快
    elif code_upper.startswith('K'):
        return 6  # 快速
    else:
        return 7  # 普快


def parse_time(time_str):
    """解析时间字符串"""
    if not time_str or time_str == '--' or time_str == '----':
        return None
    try:
        parts = time_str.split(':')
        if len(parts) == 2:
            hour = int(parts[0])
            minute = int(parts[1])
            return f"{hour:02d}:{minute:02d}:00"
    except:
        pass
    return None


def generate_sql():
    """生成SQL文件"""
    os.makedirs(OUTPUT_DIR, exist_ok=True)

    # 车站映射 (station_name -> station_id from existing data)
    station_map = {}
    existing_stations_file = os.path.join(DATA_DIR, 'stations_12306.sql')
    if os.path.exists(existing_stations_file):
        with open(existing_stations_file, 'r', encoding='utf-8') as f:
            content = f.read()
            # 简单的车站名提取
            import re
            matches = re.findall(r"\('([^']+)',\s*'([^']+)'", content)
            for code, name in matches:
                station_map[name] = code

    trains_sql = []
    train_stops_sql = []

    train_count = 0
    stop_count = 0
    skip_count = 0

    # 处理所有列车数据
    for train_type in ['G', 'D', 'C', 'K', 'T', 'Z', 'P', 'S', 'Y']:
        json_dir = os.path.join(EXTRACTED_DIR, train_type)
        if not os.path.exists(json_dir):
            print(f"目录不存在: {json_dir}")
            continue

        print(f"正在处理 {train_type} 类车次...")

        json_files = glob.glob(os.path.join(json_dir, '*.json'))
        total_files = len(json_files)
        processed = 0

        for json_file in json_files:
            try:
                with open(json_file, 'r', encoding='utf-8') as f:
                    data = json.load(f)

                stations_data = data.get('data', {}).get('data', [])
                if not stations_data:
                    skip_count += 1
                    continue

                # 获取列车基本信息
                first_station = stations_data[0]
                train_code = first_station.get('station_train_code', '').strip()

                if not train_code:
                    skip_count += 1
                    continue

                start_station = first_station.get('start_station_name', '')
                end_station = first_station.get('end_station_name', '')

                # 计算总耗时
                start_time = None
                end_time = None
                total_time = 0

                if stations_data:
                    first_depart = parse_time(stations_data[0].get('start_time', ''))
                    last_arrive = parse_time(stations_data[-1].get('arrive_time', ''))
                    if first_depart and last_arrive:
                        try:
                            t1 = datetime.strptime(first_depart, '%H:%M:%S')
                            t2 = datetime.strptime(last_arrive, '%H:%M:%S')
                            diff = (t2 - t1).total_seconds() / 60
                            total_time = int(diff)
                            start_time = first_depart
                            end_time = last_arrive
                        except:
                            pass

                train_type_id = get_train_type(train_code)

                # 生成 trains 表 SQL
                trains_sql.append(f"""('{train_code}', {train_type_id}, '{start_station}', '{end_station}', '{start_time}', '{end_time}', {total_time}, 1)""")

                # 生成 train_stops 表 SQL
                for station_info in stations_data:
                    station_name = station_info.get('station_name', '')
                    stop_order = int(station_info.get('station_no', 0))
                    arrive_time = parse_time(station_info.get('arrive_time', '')) or 'NULL'
                    depart_time = parse_time(station_info.get('start_time', '')) or 'NULL'
                    is_start = 1 if station_info.get('is_start') == 'Y' else 0
                    is_end = 1 if station_name == end_station else 0

                    # arrive_time 和 depart_time 如果是字符串需要加引号
                    arrive_str = f"'{arrive_time}'" if arrive_time != 'NULL' else 'NULL'
                    depart_str = f"'{depart_time}'" if depart_time != 'NULL' else 'NULL'

                    train_stops_sql.append(f"""('{train_code}', '{station_name}', {stop_order}, {arrive_str}, {depart_str}, {is_start}, {is_end})""")
                    stop_count += 1

                train_count += 1
                processed += 1
                if processed % 1000 == 0:
                    print(f"  已处理 {processed}/{total_files} 个文件...")

            except Exception as e:
                skip_count += 1
                print(f"  处理失败 {json_file}: {e}")

    # 写入 trains SQL
    trains_sql_file = os.path.join(OUTPUT_DIR, 'trains_new.sql')
    with open(trains_sql_file, 'w', encoding='utf-8') as f:
        f.write("-- 12306 新车次数据\n")
        f.write("-- 生成时间: " + datetime.now().strftime('%Y-%m-%d %H:%M:%S') + "\n\n")
        f.write("USE railway_system;\n\n")
        f.write("-- 插入新车次（使用 REPLACE 处理重复）\n")
        f.write("INSERT INTO trains (train_code, train_type, start_station_name, end_station_name, start_time, end_time, total_time, status)\n")
        f.write("VALUES\n")
        f.write(',\n'.join(trains_sql))
        f.write("\nON DUPLICATE KEY UPDATE\n")
        f.write("  train_type = VALUES(train_type),\n")
        f.write("  start_station_name = VALUES(start_station_name),\n")
        f.write("  end_station_name = VALUES(end_station_name),\n")
        f.write("  start_time = VALUES(start_time),\n")
        f.write("  end_time = VALUES(end_time),\n")
        f.write("  total_time = VALUES(total_time),\n")
        f.write("  status = 1;\n")

    # 写入 train_stops SQL
    with open(TRAIN_STOPS_OUTPUT, 'w', encoding='utf-8') as f:
        f.write("-- 12306 新经停站数据\n")
        f.write("-- 生成时间: " + datetime.now().strftime('%Y-%m-%d %H:%M:%S') + "\n\n")
        f.write("USE railway_system;\n\n")
        f.write("-- 先删除旧的经停站数据（如果需要）\n")
        f.write("-- DELETE FROM train_stops;\n\n")
        f.write("-- 插入新经停站\n")
        f.write("INSERT INTO train_stops (train_code, station_name, stop_order, arrive_time, depart_time, is_start, is_end)\n")
        f.write("VALUES\n")
        f.write(',\n'.join(train_stops_sql))
        f.write("\nON DUPLICATE KEY UPDATE\n")
        f.write("  station_name = VALUES(station_name),\n")
        f.write("  stop_order = VALUES(stop_order),\n")
        f.write("  arrive_time = VALUES(arrive_time),\n")
        f.write("  depart_time = VALUES(depart_time),\n")
        f.write("  is_start = VALUES(is_start),\n")
        f.write("  is_end = VALUES(is_end);\n")

    print(f"\n生成完成!")
    print(f"  车次文件: {trains_sql_file}")
    print(f"  经停站文件: {TRAIN_STOPS_OUTPUT}")
    print(f"  新增车次: {train_count}")
    print(f"  新增经停站: {stop_count}")
    print(f"  跳过: {skip_count}")


def main():
    """主函数"""
    print("=" * 60)
    print("12306数据导入脚本 - SQL生成器")
    print("=" * 60)

    # 检查数据目录
    if not os.path.exists(EXTRACTED_DIR):
        print(f"错误: 数据目录不存在: {EXTRACTED_DIR}")
        print("请先运行: unzip utf8.zip")
        return

    print(f"\n数据目录: {EXTRACTED_DIR}")
    print(f"输出目录: {OUTPUT_DIR}")
    print()

    # 生成SQL
    generate_sql()

    print("\n" + "=" * 60)
    print("SQL文件生成完成!")
    print("请执行以下命令导入数据:")
    print(f"  mysql -u root -p123456 railway_system < {OUTPUT_DIR}/trains_new.sql")
    print(f"  mysql -u root -p123456 railway_system < {OUTPUT_DIR}/train_stops_new.sql")
    print("=" * 60)


if __name__ == '__main__':
    main()
