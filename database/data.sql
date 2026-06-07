-- 12306旅客管理系统初始化数据
-- 全国主要车站数据

USE railway_system;

-- ============================================
-- 车站数据 (主要车站)
-- ============================================

-- 北京地区
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('BJP', '北京站', '北京', 'BJ', '北京', 'beijingzhan', 'bjz', 1, 0),
('BJN', '北京南站', '北京', 'BJ', '北京', 'beijingnanzhan', 'bjnz', 1, 1),
('BXP', '北京西站', '北京', 'BJ', '北京', 'beijingxizhan', 'bjxz', 1, 1),
('BJD', '北京东站', '北京', 'BJ', '北京', 'beijingdongzhan', 'bjdz', 3, 0),
('CPN', '昌平北站', '北京', 'BJ', '北京', 'changpingbeizhan', 'cpbz', 4, 0),
('YZP', '燕郊站', '北京', 'BJ', '北京', 'yanjiaozhan', 'yjz', 4, 0);

-- 上海地区
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SHH', '上海站', '上海', 'SH', '上海', 'shanghaizhan', 'shz', 1, 1),
('SHN', '上海南站', '上海', 'SH', '上海', 'shanghainanzhan', 'shnz', 1, 0),
('AOH', '上海虹桥站', '上海', 'SH', '上海', 'shanghaihongqiao', 'shhq', 1, 1),
('SHX', '上海西站', '上海', 'SH', '上海', 'shanghaixizhan', 'shxz', 3, 0);

-- 广州地区
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('GZQ', '广州站', '广州', 'GZ', '广东', 'guangzhouzhan', 'gz', 1, 0),
('GZN', '广州南站', '广州', 'GZ', '广东', 'guangzhounanzhan', 'gzn', 1, 1),
('GZD', '广州东站', '广州', 'GZ', '广东', 'guangzhoudongzhan', 'gzdz', 1, 1);

-- 深圳
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SZQ', '深圳站', '深圳', 'SZ', '广东', 'shenzhenzhan', 'sz', 1, 0),
('SNW', '深圳北站', '深圳', 'SZ', '广东', 'shenzhenbeizhan', 'szb', 1, 1),
('SZD', '深圳东站', '深圳', 'SZ', '广东', 'shenzhendongzhan', 'szdz', 2, 0);

-- 天津
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('TJP', '天津站', '天津', 'TJ', '天津', 'tianjinzhan', 'tj', 1, 1),
('TIN', '天津南站', '天津', 'TJ', '天津', 'tianjinnanzhan', 'tjn', 1, 1),
('TXP', '天津西站', '天津', 'TJ', '天津', 'tianjinxizhan', 'tjx', 1, 1);

-- 重庆
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('CQW', '重庆站', '重庆', 'CQ', '重庆', 'chongqingzhan', 'cq', 1, 0),
('CQK', '重庆北站', '重庆', 'CQ', '重庆', 'chongqingbeizhan', 'cqb', 1, 1),
('CQS', '重庆西站', '重庆', 'CQ', '重庆', 'chongqingxizhan', 'cqx', 1, 1);

-- 成都
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('CDW', '成都站', '成都', 'CD', '四川', 'chengduzhan', 'cd', 1, 0),
('CDN', '成都南站', '成都', 'CD', '四川', 'chengdunanzhan', 'cdn', 2, 1),
('CDX', '成都西站', '成都', 'CD', '四川', 'chengduxizhan', 'cdx', 2, 1),
('CDE', '成都东站', '成都', 'CD', '四川', 'chengdudongzhan', 'cdd', 1, 1);

-- 武汉
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('WHN', '武汉站', '武汉', 'WH', '湖北', 'wuhanzhan', 'wh', 1, 1),
('WHK', '武汉北站', '武汉', 'WH', '湖北', 'wuhanbeizhan', 'whb', 2, 0),
('HKN', '汉口站', '武汉', 'WH', '湖北', 'hankouzhan', 'hk', 1, 1),
('WCN', '武昌站', '武汉', 'WH', '湖北', 'wuchangzhan', 'wc', 1, 0);

-- 南京
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('NJH', '南京站', '南京', 'NJ', '江苏', 'nanjingzhan', 'nj', 1, 0),
('NJN', '南京南站', '南京', 'NJ', '江苏', 'nanjingnanzhan', 'njn', 1, 1);

-- 杭州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HZH', '杭州站', '杭州', 'HZ', '浙江', 'hangzhouzhan', 'hz', 1, 0),
('HGH', '杭州东站', '杭州', 'HZ', '浙江', 'hangzhoudongzhan', 'hzd', 1, 1),
('HZN', '杭州南站', '杭州', 'HZ', '浙江', 'hangzhounanzhan', 'hzn', 2, 1);

-- 西安
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('XAY', '西安站', '西安', 'XA', '陕西', 'xianzhan', 'xa', 1, 0),
('XAE', '西安北站', '西安', 'XA', '陕西', 'xianbeizhan', 'xab', 1, 1);

-- 郑州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('ZZF', '郑州站', '郑州', 'ZZ', '河南', 'zhengzhouzhan', 'zz', 1, 0),
('ZZD', '郑州东站', '郑州', 'ZZ', '河南', 'zhengzhoudongzhan', 'zzd', 1, 1);

-- 长沙
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('CSQ', '长沙站', '长沙', 'CS', '湖南', 'changshazhan', 'cs', 1, 0),
('CWQ', '长沙南站', '长沙', 'CS', '湖南', 'changshananzhan', 'csn', 1, 1);

-- 沈阳
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SYT', '沈阳站', '沈阳', 'SY', '辽宁', 'shenyangzhan', 'sy', 1, 1),
('SYB', '沈阳北站', '沈阳', 'SY', '辽宁', 'shenyangbeizhan', 'syb', 1, 1);

-- 大连
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('DLT', '大连站', '大连', 'DL', '辽宁', 'dalianzhan', 'dl', 1, 0),
('DLB', '大连北站', '大连', 'DL', '辽宁', 'dalianbeizhan', 'dlb', 1, 1);

-- 青岛
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('QDK', '青岛站', '青岛', 'QD', '山东', 'qingdaozhan', 'qd', 1, 1),
('QDB', '青岛北站', '青岛', 'QD', '山东', 'qingdaobeizhan', 'qdb', 1, 1);

-- 济南
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('JNK', '济南站', '济南', 'JN', '山东', 'jinanzhan', 'jn', 1, 0),
('JGK', '济南东站', '济南', 'JN', '山东', 'jinandongzhan', 'jnd', 1, 1),
('JNK', '济南西站', '济南', 'JN', '山东', 'jinanxizhan', 'jnx', 1, 1);

-- 厦门
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('XMK', '厦门站', '厦门', 'XM', '福建', 'xiamenzhan', 'xm', 1, 1),
('XMB', '厦门北站', '厦门', 'XM', '福建', 'xiamenbeizhan', 'xmb', 1, 1);

-- 福州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('FZS', '福州站', '福州', 'FZ', '福建', 'fuzhouzhan', 'fz', 1, 1),
('FOS', '福州南站', '福州', 'FZ', '福建', 'fuzhounanzhan', 'fzn', 1, 1);

-- 苏州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SZH', '苏州站', '苏州', 'SZ', '江苏', 'suzhouzhan', 'sz', 1, 1),
('SZB', '苏州北站', '苏州', 'SZ', '江苏', 'suzhoubeizhan', 'szb', 2, 1);

-- 无锡
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('WXH', '无锡站', '无锡', 'WX', '江苏', 'wuxizhan', 'wx', 1, 1),
('WXB', '无锡东站', '无锡', 'WX', '江苏', 'wuxidongzhan', 'wxd', 1, 1);

-- 昆明
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('KMM', '昆明站', '昆明', 'KM', '云南', 'kunmingzhan', 'km', 1, 0),
('KMS', '昆明南站', '昆明', 'KM', '云南', 'kunmingnanzhan', 'kmn', 1, 1);

-- 南宁
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('NNZ', '南宁站', '南宁', 'NN', '广西', 'nanningzhan', 'nn', 1, 0),
('NNY', '南宁东站', '南宁', 'NN', '广西', 'nanningdongzhan', 'nnd', 1, 1);

-- 贵阳
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('GIW', '贵阳站', '贵阳', 'GY', '贵州', 'guiyangzhan', 'gy', 1, 0),
('GZE', '贵阳北站', '贵阳', 'GY', '贵州', 'guiyangbeizhan', 'gyb', 1, 1),
('GYN', '贵阳东站', '贵阳', 'GY', '贵州', 'guiyangdongzhan', 'gyd', 2, 1);

-- 兰州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('LZJ', '兰州站', '兰州', 'LZ', '甘肃', 'lanzhouzhan', 'lz', 1, 0),
('LZB', '兰州西站', '兰州', 'LZ', '甘肃', 'lanzhouxizhan', 'lzx', 1, 1);

-- 哈尔滨站
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HBB', '哈尔滨站', '哈尔滨', 'HB', '黑龙江', 'haerbinzhan', 'heb', 1, 1),
('HBD', '哈尔滨东站', '哈尔滨', 'HB', '黑龙江', 'haerbindongzhan', 'hebd', 2, 0),
('HBK', '哈尔滨西站', '哈尔滨', 'HB', '黑龙江', 'haerbinxizhan', 'hebx', 1, 1);

-- 长春
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('CCT', '长春站', '长春', 'CC', '吉林', 'changchunzhan', 'cc', 1, 1),
('CCE', '长春西站', '长春', 'CC', '吉林', 'changchunxizhan', 'ccx', 1, 1);

-- 石家庄
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SJP', '石家庄站', '石家庄', 'SJZ', '河北', 'shijiazhuangzhan', 'sjz', 1, 1),
('SJP', '石家庄北站', '石家庄', 'SJZ', '河北', 'shijiazhuangbeizhan', 'sjzb', 2, 0);

-- 太原
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('TYV', '太原站', '太原', 'TY', '山西', 'taiyuanzhan', 'ty', 1, 0),
('TYN', '太原南站', '太原', 'TY', '山西', 'taiyuannanzhan', 'ty', 1, 1);

-- 呼和浩特
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HHC', '呼和浩特站', '呼和浩特', 'HHHT', '内蒙古', 'huhehaotezhan', 'hhht', 1, 0),
('HHO', '呼和浩特东站', '呼和浩特', 'HHHT', '内蒙古', 'huhehaotedongzhan', 'hhhtd', 1, 1);

-- 乌鲁木齐
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('WMR', '乌鲁木齐站', '乌鲁木齐', 'WLMQ', '新疆', 'wulumuqizhan', 'wlmq', 1, 1);

-- 合肥
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HFH', '合肥站', '合肥', 'HF', '安徽', 'hefeizhan', 'hf', 1, 0),
('HFB', '合肥南站', '合肥', 'HF', '安徽', 'hefeinanzhan', 'hfn', 1, 1);

-- 南昌
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('NCG', '南昌站', '南昌', 'NC', '江西', 'nanchangzhan', 'nc', 1, 0),
('NCG', '南昌西站', '南昌', 'NC', '江西', 'nanchangxizhan', 'ncx', 1, 1);

-- 宁波
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('NGH', '宁波站', '宁波', 'NB', '浙江', 'ningbozhan', 'nb', 1, 1);

-- 温州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('WNH', '温州站', '温州', 'WZ', '浙江', 'wenzhouzhan', 'wz', 1, 0),
('WZH', '温州南站', '温州', 'WZ', '浙江', 'wenzhounanzhan', 'wzn', 1, 1);

-- 常州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('CZH', '常州站', '常州', 'CZ', '江苏', 'changzhouzhan', 'cz', 1, 1),
('CZE', '常州北站', '常州', 'CZ', '江苏', 'changzhoubeizhan', 'czb', 2, 1);

-- 徐州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('XCH', '徐州站', '徐州', 'XZ', '江苏', 'xuzhouzhan', 'xz', 1, 0),
('XXH', '徐州东站', '徐州', 'XZ', '江苏', 'xuzhoudongzhan', 'xzd', 1, 1);

-- 连云港
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('UIH', '连云港站', '连云港', 'LYG', '江苏', 'lianyungangzhan', 'lyg', 1, 1);

-- 蚌埠
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('BBH', '蚌埠站', '蚌埠', 'BB', '安徽', 'bengbuzhan', 'bb', 1, 0),
('BBD', '蚌埠南站', '蚌埠', 'BB', '安徽', 'bengbunanzhan', 'bbn', 1, 1);

-- 潍坊
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('WFK', '潍坊站', '潍坊', 'WF', '山东', 'weifangzhan', 'wf', 1, 1);

-- 烟台
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('YTK', '烟台站', '烟台', 'YT', '山东', 'yantai', 'yt', 1, 1);

-- 嘉兴
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('JXH', '嘉兴站', '嘉兴', 'JX', '浙江', 'jiaxingzhan', 'jx', 2, 0),
('JAH', '嘉兴南站', '嘉兴', 'JX', '浙江', 'jiaxingnanzhan', 'jxn', 2, 1);

-- 绍兴
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SOH', '绍兴站', '绍兴', 'SX', '浙江', 'shaoxingzhan', 'sx', 2, 0),
('SLH', '绍兴北站', '绍兴', 'SX', '浙江', 'shaoxingbeizhan', 'sxb', 2, 1);

-- 台州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('TUH', '台州站', '台州', 'TZ', '浙江', 'taizhouzhan', 'tz', 1, 1);

-- 金华
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('JHH', '金华站', '金华', 'JH', '浙江', 'jinhua', 'jh', 1, 1);

-- 义乌
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('YWH', '义乌站', '义乌', 'YW', '浙江', 'yiwu', 'yw', 2, 1);

-- 惠州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HCQ', '惠州站', '惠州', 'HZ', '广东', 'huizhou', 'hz', 2, 0);

-- 东莞
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('DMA', '东莞站', '东莞', 'DG', '广东', 'dongguanzhan', 'dg', 2, 1),
('DOA', '东莞东站', '东莞', 'DG', '广东', 'dongguandongzhan', 'dgd', 2, 0);

-- 珠海
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('ZHQ', '珠海站', '珠海', 'ZH', '广东', 'zhuhai', 'zh', 1, 1);

-- 中山
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('ZSQ', '中山站', '中山', 'ZS', '广东', 'zhongshan', 'zs', 1, 1);

-- 汕头
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SBQ', '汕头站', '汕头', 'ST', '广东', 'shantou', 'st', 1, 1);

-- 佛山
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('FOQ', '佛山站', '佛山', 'FS', '广东', 'foshan', 'fs', 1, 0);

-- 湛江
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('ZJQ', '湛江站', '湛江', 'ZJ', '广东', 'zhanjiang', 'zj', 1, 0);

-- 洛阳
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('LYF', '洛阳站', '洛阳', 'LY', '河南', 'luoyang', 'ly', 1, 0),
('LAF', '洛阳龙门站', '洛阳', 'LY', '河南', 'luoyanglongmen', 'lylm', 1, 1);

-- 开封
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('KFF', '开封站', '开封', 'KF', '河南', 'kaifeng', 'kf', 2, 0);

-- 新乡
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('XXF', '新乡站', '新乡', 'XX', '河南', 'xinxiang', 'xx', 1, 0),
('XXE', '新乡东站', '新乡', 'XX', '河南', 'xinxiangdong', 'xxd', 1, 1);

-- 安阳
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('AYF', '安阳站', '安阳', 'AY', '河南', 'anyang', 'ay', 1, 0),
('AYD', '安阳东站', '安阳', 'AY', '河南', 'anyangdong', 'ayd', 1, 1);

-- 邯郸
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HDP', '邯郸站', '邯郸', 'HD', '河北', 'handan', 'hd', 1, 0),
('HAV', '邯郸东站', '邯郸', 'HD', '河北', 'handandong', 'hdd', 1, 1);

-- 邢台
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('XTP', '邢台站', '邢台', 'XT', '河北', 'xingtai', 'xt', 1, 0);

-- 保定
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('BDP', '保定站', '保定', 'BD', '河北', 'baoding', 'bd', 1, 0),
('BDQ', '保定东站', '保定', 'BD', '河北', 'baodingdong', 'bdd', 1, 1);

-- 廊坊
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('LFP', '廊坊站', '廊坊', 'LF', '河北', 'langfang', 'lf', 2, 1);

-- 衡水
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HSP', '衡水站', '衡水', 'HS', '河北', 'hengshui', 'hs', 1, 0);

-- 德州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('DZK', '德州站', '德州', 'DZ', '山东', 'dezhou', 'dz', 1, 0),
('DJP', '德州东站', '德州', 'DZ', '山东', 'dezhoudong', 'dzd', 1, 1);

-- 泰安
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('TAK', '泰安站', '泰安', 'TA', '山东', 'taian', 'ta', 1, 1);

-- 曲阜
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('QFK', '曲阜站', '曲阜', 'QF', '山东', 'qufu', 'qf', 2, 0),
('QFK', '曲阜东站', '曲阜', 'QF', '山东', 'qufudong', 'qfd', 1, 1);

-- 日照
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('RZK', '日照站', '日照', 'RZ', '山东', 'rizhao', 'rz', 1, 0);

-- 威海
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('WAK', '威海站', '威海', 'WH', '山东', 'weihai', 'wh', 1, 1);

-- 滕州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('TZK', '滕州站', '滕州', 'TZ', '山东', 'tengzhou', 'tz', 2, 0);

-- 枣庄
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('ZEK', '枣庄站', '枣庄', 'ZZ', '山东', 'zaozhuang', 'zz', 1, 1);

-- 宿州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SXH', '宿州站', '宿州', 'SZ', '安徽', 'suzhou', 'sz', 2, 0);

-- 淮北
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HBH', '淮北站', '淮北', 'HB', '安徽', 'huaibei', 'hb', 2, 0);

-- 淮南
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HNH', '淮南站', '淮南', 'HN', '安徽', 'huainan', 'hn', 1, 0);

-- 马鞍山
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('MAH', '马鞍山站', '马鞍山', 'MAS', '安徽', 'maanshan', 'mas', 2, 0);

-- 铜陵
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('TLH', '铜陵站', '铜陵', 'TL', '安徽', 'tongling', 'tl', 2, 1);

-- 安庆
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('AQH', '安庆站', '安庆', 'AQ', '安徽', 'anqing', 'aq', 1, 1);

-- 黄山
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HSH', '黄山站', '黄山', 'HS', '安徽', 'huangshan', 'hs', 1, 0);

-- 九江
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('JGK', '九江站', '九江', 'JJ', '江西', 'jiujiang', 'jj', 1, 1);

-- 上饶
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SRG', '上饶站', '上饶', 'SR', '江西', 'shangrao', 'sr', 1, 1);

-- 赣州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('GZG', '赣州站', '赣州', 'GZ', '江西', 'ganzhou', 'gz', 1, 0);

-- 宜春
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('YCG', '宜春站', '宜春', 'YC', '江西', 'yichun', 'yc', 1, 1);

-- 吉安
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('VAG', '吉安站', '吉安', 'JA', '江西', 'jian', 'ja', 1, 0);

-- 萍乡
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('PXG', '萍乡站', '萍乡', 'PX', '江西', 'pingxiang', 'px', 1, 0);

-- 新余
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('XYG', '新余站', '新余', 'XY', '江西', 'xinyu', 'xy', 1, 0);

-- 衡阳
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HYQ', '衡阳站', '衡阳', 'HY', '湖南', 'hengyang', 'hy', 1, 0),
('HYQ', '衡阳东站', '衡阳', 'HY', '湖南', 'hengyangdong', 'hyd', 1, 1);

-- 株洲
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('ZZQ', '株洲站', '株洲', 'ZZ', '湖南', 'zhuzhou', 'zz', 1, 0);

-- 湘潭
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('XTQ', '湘潭站', '湘潭', 'XT', '湖南', 'xiangtan', 'xt', 1, 1);

-- 岳阳
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('YYQ', '岳阳站', '岳阳', 'YY', '湖南', 'yueyang', 'yy', 1, 0),
('YWA', '岳阳东站', '岳阳', 'YY', '湖南', 'yueyangdong', 'yyd', 1, 1);

-- 怀化
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HPQ', '怀化站', '怀化', 'HH', '湖南', 'huaihua', 'hh', 1, 0),
('HPA', '怀化南站', '怀化', 'HH', '湖南', 'huaihuanan', 'hhn', 1, 1);

-- 邵阳
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SYQ', '邵阳站', '邵阳', 'SY', '湖南', 'shaoyang', 'sy', 1, 1);

-- 常德
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('CDQ', '常德站', '常德', 'CD', '湖南', 'changde', 'cd', 1, 1);

-- 永州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('YZQ', '永州站', '永州', 'YZ', '湖南', 'yongzhou', 'yz', 1, 0);

-- 郴州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('CZQ', '郴州站', '郴州', 'CZ', '湖南', 'chenzhou', 'cz', 1, 0),
('CWA', '郴州西站', '郴州', 'CZ', '湖南', 'chenzhouxi', 'czx', 1, 1);

-- 湛江
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('ZJQ', '湛江站', '湛江', 'ZJ', '广东', 'zhanjiang', 'zj', 1, 0);

-- 茂名
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('MDQ', '茂名站', '茂名', 'MM', '广东', 'maoming', 'mm', 1, 1);

-- 阳江
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('YJQ', '阳江站', '阳江', 'YJ', '广东', 'yangjiang', 'yj', 1, 1);

-- 肇庆
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('ZQY', '肇庆站', '肇庆', 'ZQ', '广东', 'zhaoqing', 'zq', 2, 0);

-- 清远
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('QYA', '清远站', '清远', 'QY', '广东', 'qingyuan', 'qy', 2, 1);

-- 潮州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('CBQ', '潮州站', '潮州', 'CZ', '广东', 'chaozhou', 'cz', 2, 0);

-- 揭阳
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('JYA', '揭阳站', '揭阳', 'JY', '广东', 'jieyang', 'jy', 1, 1);

-- 河源
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('HEY', '河源站', '河源', 'HY', '广东', 'heyuan', 'hy', 1, 0);

-- 梅州
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('MXQ', '梅州站', '梅州', 'MZ', '广东', 'meizhou', 'mz', 1, 0);

-- 韶关
INSERT INTO stations (station_code, station_name, city_name, city_code, province, station_pinyin, station_short_pinyin, station_level, is_high_speed) VALUES
('SGQ', '韶关站', '韶关', 'SG', '广东', 'shaoguan', 'sg', 1, 0),
('SGA', '韶关东站', '韶关', 'SG', '广东', 'shaoguandong', 'sgd', 1, 1);

-- ============================================
-- 模拟车次数据 (主要高铁线路)
-- ============================================

-- 京沪高铁 G字头
INSERT INTO trains (train_code, train_type, start_station_id, end_station_id, start_station_name, end_station_name, start_time, end_time, total_time, total_distance, total_stops, has_seat_selection) VALUES
('G1', 1, 2, 8, '北京南站', '上海虹桥站', '07:00:00', '11:30:00', 270, 1318, 3, 1),
('G2', 1, 8, 2, '上海虹桥站', '北京南站', '07:00:00', '11:30:00', 270, 1318, 3, 1),
('G3', 1, 2, 8, '北京南站', '上海虹桥站', '08:00:00', '12:48:00', 288, 1318, 5, 1),
('G4', 1, 8, 2, '上海虹桥站', '北京南站', '08:00:00', '12:48:00', 288, 1318, 5, 1),
('G5', 1, 2, 8, '北京南站', '上海虹桥站', '09:00:00', '13:28:00', 268, 1318, 2, 1),
('G6', 1, 8, 2, '上海虹桥站', '北京南站', '09:00:00', '13:28:00', 268, 1318, 2, 1),
('G7', 1, 2, 8, '北京南站', '上海虹桥站', '10:00:00', '14:55:00', 295, 1318, 6, 1),
('G8', 1, 8, 2, '上海虹桥站', '北京南站', '10:00:00', '14:55:00', 295, 1318, 6, 1),
('G9', 1, 2, 8, '北京南站', '上海虹桥站', '11:00:00', '15:30:00', 270, 1318, 4, 1),
('G10', 1, 8, 2, '上海虹桥站', '北京南站', '11:00:00', '15:30:00', 270, 1318, 4, 1),
('G11', 1, 2, 8, '北京南站', '上海虹桥站', '12:00:00', '16:48:00', 288, 1318, 5, 1),
('G12', 1, 8, 2, '上海虹桥站', '北京南站', '12:00:00', '16:48:00', 288, 1318, 5, 1),
('G13', 1, 2, 8, '北京南站', '上海虹桥站', '13:00:00', '17:28:00', 268, 1318, 3, 1),
('G14', 1, 8, 2, '上海虹桥站', '北京南站', '13:00:00', '17:28:00', 268, 1318, 3, 1),
('G15', 1, 2, 8, '北京南站', '上海虹桥站', '14:00:00', '18:55:00', 295, 1318, 6, 1),
('G16', 1, 8, 2, '上海虹桥站', '北京南站', '14:00:00', '18:55:00', 295, 1318, 6, 1),
('G17', 1, 2, 8, '北京南站', '上海虹桥站', '15:00:00', '19:30:00', 270, 1318, 4, 1),
('G18', 1, 8, 2, '上海虹桥站', '北京南站', '15:00:00', '19:30:00', 270, 1318, 4, 1),
('G19', 1, 2, 8, '北京南站', '上海虹桥站', '16:00:00', '20:28:00', 268, 1318, 3, 1),
('G20', 1, 8, 2, '上海虹桥站', '北京南站', '16:00:00', '20:28:00', 268, 1318, 3, 1);

-- 京广高铁 G字头
INSERT INTO trains (train_code, train_type, start_station_id, end_station_id, start_station_name, end_station_name, start_time, end_time, total_time, total_distance, total_stops, has_seat_selection) VALUES
('G21', 1, 2, 21, '北京南站', '广州南站', '07:00:00', '15:00:00', 480, 2298, 10, 1),
('G22', 1, 21, 2, '广州南站', '北京南站', '07:00:00', '15:00:00', 480, 2298, 10, 1),
('G23', 1, 2, 21, '北京南站', '广州南站', '08:00:00', '16:30:00', 510, 2298, 12, 1),
('G24', 1, 21, 2, '广州南站', '北京南站', '08:00:00', '16:30:00', 510, 2298, 12, 1),
('G25', 1, 2, 21, '北京南站', '广州南站', '09:00:00', '17:00:00', 480, 2298, 8, 1),
('G26', 1, 21, 2, '广州南站', '北京南站', '09:00:00', '17:00:00', 480, 2298, 8, 1),
('G27', 1, 2, 21, '北京南站', '广州南站', '10:00:00', '18:30:00', 510, 2298, 11, 1),
('G28', 1, 21, 2, '广州南站', '北京南站', '10:00:00', '18:30:00', 510, 2298, 11, 1),
('G29', 1, 2, 21, '北京南站', '广州南站', '11:00:00', '19:00:00', 480, 2298, 9, 1),
('G30', 1, 21, 2, '广州南站', '北京南站', '11:00:00', '19:00:00', 480, 2298, 9, 1);

-- 京沪高铁经过站点
INSERT INTO train_stops (train_id, train_code, station_id, station_name, stop_order, arrive_time, depart_time, stop_duration, distance, is_start, is_end) VALUES
-- G1 北京南-上海虹桥
(1, 'G1', 2, '北京南站', 1, NULL, '07:00:00', 2, 0, 1, 0),
(1, 'G1', 18, '济南西站', 2, '08:32:00', '08:34:00', 2, 406, 0, 0),
(1, 'G1', 26, '南京南站', 3, '10:20:00', '10:22:00', 2, 1023, 0, 0),
(1, 'G1', 8, '上海虹桥站', 4, '11:30:00', NULL, 2, 1318, 0, 1),
-- G2 上海虹桥-北京南
(2, 'G2', 8, '上海虹桥站', 1, NULL, '07:00:00', 2, 0, 1, 0),
(2, 'G2', 26, '南京南站', 2, '08:02:00', '08:04:00', 2, 295, 0, 0),
(2, 'G2', 18, '济南西站', 3, '09:34:00', '09:36:00', 2, 912, 0, 0),
(2, 'G2', 2, '北京南站', 4, '11:30:00', NULL, 2, 1318, 0, 1);

-- 京广高铁经过站点
INSERT INTO train_stops (train_id, train_code, station_id, station_name, stop_order, arrive_time, depart_time, stop_duration, distance, is_start, is_end) VALUES
-- G21 北京南-广州南
(21, 'G21', 2, '北京南站', 1, NULL, '07:00:00', 2, 0, 1, 0),
(21, 'G21', 39, '石家庄站', 2, '08:11:00', '08:13:00', 2, 277, 0, 0),
(21, 'G21', 40, '郑州东站', 3, '09:30:00', '09:32:00', 2, 636, 0, 0),
(21, 'G21', 43, '武汉站', 4, '11:45:00', '11:47:00', 2, 1229, 0, 0),
(21, 'G21', 41, '长沙南站', 5, '13:15:00', '13:17:00', 2, 1586, 0, 0),
(21, 'G21', 21, '广州南站', 6, '15:00:00', NULL, 2, 2298, 0, 1);

-- ============================================
-- 公告数据
-- ============================================
INSERT INTO announcements (title, content, announcement_type, is_top, is_active, publish_time) VALUES
('欢迎使用12306旅客管理系统', '本系统为铁路旅客管理系统演示版本，提供车票查询、购票、改签、退票等功能。请先注册账号并添加旅客信息后进行购票操作。', 1, 1, 1, NOW()),
('购票须知', '1. 每个账号最多可添加15名常用联系人。\n2. 购票后请在30分钟内完成支付，否则订单将自动取消。\n3. 退票手续费：开车前48小时以上退票收取5%，24-48小时收取10%，不足24小时收取20%。', 2, 0, 1, NOW()),
('实名认证说明', '根据铁路部门规定，购票需进行实名认证。请确保旅客信息真实有效，姓名和身份证号需与身份证一致。', 3, 0, 1, NOW());