CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY,
    keycloak_user_id VARCHAR(64) NOT NULL,
    dept_id BIGINT,
    user_name VARCHAR(64) NOT NULL,
    nick_name VARCHAR(64) NOT NULL,
    user_type VARCHAR(16) NOT NULL DEFAULT 'SYSTEM',
    email VARCHAR(128),
    phone_number VARCHAR(32),
    sex CHAR(1) DEFAULT '0',
    avatar VARCHAR(512),
    status CHAR(1) NOT NULL DEFAULT '0',
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    last_sync_time TIMESTAMPTZ,
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_user_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_user_del_flag CHECK (del_flag IN ('0', '1')),
    CONSTRAINT ck_sys_user_sex CHECK (sex IN ('0', '1', '2'))
);

CREATE TABLE sys_dept (
    id BIGINT PRIMARY KEY,
    parent_id BIGINT NOT NULL DEFAULT 0,
    ancestors VARCHAR(500) NOT NULL DEFAULT '0',
    dept_name VARCHAR(128) NOT NULL,
    order_num INTEGER NOT NULL DEFAULT 0,
    leader_user_id BIGINT,
    phone VARCHAR(32),
    email VARCHAR(128),
    status CHAR(1) NOT NULL DEFAULT '0',
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_dept_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_dept_del_flag CHECK (del_flag IN ('0', '1'))
);

CREATE TABLE sys_post (
    id BIGINT PRIMARY KEY,
    post_code VARCHAR(64) NOT NULL,
    post_name VARCHAR(128) NOT NULL,
    post_sort INTEGER NOT NULL DEFAULT 0,
    status CHAR(1) NOT NULL DEFAULT '0',
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_post_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_post_del_flag CHECK (del_flag IN ('0', '1'))
);

CREATE TABLE sys_menu (
    id BIGINT PRIMARY KEY,
    menu_name VARCHAR(128) NOT NULL,
    parent_id BIGINT NOT NULL DEFAULT 0,
    order_num INTEGER NOT NULL DEFAULT 0,
    path VARCHAR(255),
    component VARCHAR(255),
    query_param VARCHAR(255),
    route_name VARCHAR(128),
    is_frame CHAR(1) NOT NULL DEFAULT 'N',
    is_cache CHAR(1) NOT NULL DEFAULT 'N',
    menu_type CHAR(1) NOT NULL,
    visible CHAR(1) NOT NULL DEFAULT 'Y',
    status CHAR(1) NOT NULL DEFAULT '0',
    permission_code VARCHAR(128),
    icon VARCHAR(128),
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_menu_is_frame CHECK (is_frame IN ('Y', 'N')),
    CONSTRAINT ck_sys_menu_is_cache CHECK (is_cache IN ('Y', 'N')),
    CONSTRAINT ck_sys_menu_menu_type CHECK (menu_type IN ('M', 'C', 'F')),
    CONSTRAINT ck_sys_menu_visible CHECK (visible IN ('Y', 'N')),
    CONSTRAINT ck_sys_menu_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_menu_del_flag CHECK (del_flag IN ('0', '1'))
);

CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY,
    role_name VARCHAR(128) NOT NULL,
    role_key VARCHAR(128) NOT NULL,
    role_sort INTEGER NOT NULL DEFAULT 0,
    data_scope CHAR(1) NOT NULL DEFAULT '1',
    menu_check_strictly CHAR(1) NOT NULL DEFAULT 'Y',
    dept_check_strictly CHAR(1) NOT NULL DEFAULT 'Y',
    status CHAR(1) NOT NULL DEFAULT '0',
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_role_data_scope CHECK (data_scope IN ('1', '2', '3', '4', '5')),
    CONSTRAINT ck_sys_role_menu_strict CHECK (menu_check_strictly IN ('Y', 'N')),
    CONSTRAINT ck_sys_role_dept_strict CHECK (dept_check_strictly IN ('Y', 'N')),
    CONSTRAINT ck_sys_role_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_role_del_flag CHECK (del_flag IN ('0', '1'))
);

CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500)
);

CREATE TABLE sys_user_post (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500)
);

CREATE TABLE sys_role_menu (
    id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500)
);

CREATE TABLE sys_role_dept (
    id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    dept_id BIGINT NOT NULL,
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500)
);

CREATE TABLE sys_dict_type (
    id BIGINT PRIMARY KEY,
    dict_name VARCHAR(128) NOT NULL,
    dict_type VARCHAR(128) NOT NULL,
    status CHAR(1) NOT NULL DEFAULT '0',
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_dict_type_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_dict_type_del_flag CHECK (del_flag IN ('0', '1'))
);

CREATE TABLE sys_dict_data (
    id BIGINT PRIMARY KEY,
    dict_sort INTEGER NOT NULL DEFAULT 0,
    dict_label VARCHAR(128) NOT NULL,
    dict_value VARCHAR(128) NOT NULL,
    dict_type VARCHAR(128) NOT NULL,
    css_class VARCHAR(128),
    list_class VARCHAR(128),
    is_default CHAR(1) NOT NULL DEFAULT 'N',
    status CHAR(1) NOT NULL DEFAULT '0',
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_dict_data_default CHECK (is_default IN ('Y', 'N')),
    CONSTRAINT ck_sys_dict_data_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_dict_data_del_flag CHECK (del_flag IN ('0', '1'))
);

CREATE TABLE sys_config (
    id BIGINT PRIMARY KEY,
    config_name VARCHAR(128) NOT NULL,
    config_key VARCHAR(128) NOT NULL,
    config_value VARCHAR(1000),
    config_type CHAR(1) NOT NULL DEFAULT 'N',
    status CHAR(1) NOT NULL DEFAULT '0',
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_config_type CHECK (config_type IN ('Y', 'N')),
    CONSTRAINT ck_sys_config_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_config_del_flag CHECK (del_flag IN ('0', '1'))
);

CREATE TABLE sys_notice (
    id BIGINT PRIMARY KEY,
    notice_title VARCHAR(255) NOT NULL,
    notice_type CHAR(1) NOT NULL,
    notice_content TEXT,
    status CHAR(1) NOT NULL DEFAULT '0',
    publish_time TIMESTAMPTZ,
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_notice_type CHECK (notice_type IN ('1', '2')),
    CONSTRAINT ck_sys_notice_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_notice_del_flag CHECK (del_flag IN ('0', '1'))
);

CREATE TABLE sys_oper_log (
    id BIGINT PRIMARY KEY,
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    trace_id VARCHAR(64),
    module_title VARCHAR(128),
    business_type VARCHAR(64),
    method VARCHAR(255),
    request_method VARCHAR(16),
    operator_type VARCHAR(32),
    oper_user_id BIGINT,
    oper_name VARCHAR(128),
    dept_id BIGINT,
    oper_url VARCHAR(512),
    oper_ip VARCHAR(64),
    oper_location VARCHAR(255),
    oper_param TEXT,
    json_result TEXT,
    status CHAR(1) NOT NULL DEFAULT '0',
    error_msg TEXT,
    cost_time_ms BIGINT,
    oper_time TIMESTAMPTZ NOT NULL,
    CONSTRAINT ck_sys_oper_log_status CHECK (status IN ('0', '1'))
);

CREATE TABLE sys_login_log (
    id BIGINT PRIMARY KEY,
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    keycloak_user_id VARCHAR(64),
    user_name VARCHAR(64),
    ipaddr VARCHAR(64),
    login_location VARCHAR(255),
    browser VARCHAR(128),
    os VARCHAR(128),
    status CHAR(1) NOT NULL DEFAULT '0',
    msg VARCHAR(1000),
    login_time TIMESTAMPTZ NOT NULL,
    CONSTRAINT ck_sys_login_log_status CHECK (status IN ('0', '1'))
);

CREATE TABLE sys_job (
    id BIGINT PRIMARY KEY,
    job_name VARCHAR(128) NOT NULL,
    job_group VARCHAR(128) NOT NULL,
    invoke_target VARCHAR(500) NOT NULL,
    cron_expression VARCHAR(128) NOT NULL,
    misfire_policy VARCHAR(32) NOT NULL DEFAULT 'SMART',
    concurrent CHAR(1) NOT NULL DEFAULT 'N',
    status CHAR(1) NOT NULL DEFAULT '0',
    del_flag CHAR(1) NOT NULL DEFAULT '0',
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMPTZ,
    remark VARCHAR(500),
    CONSTRAINT ck_sys_job_concurrent CHECK (concurrent IN ('Y', 'N')),
    CONSTRAINT ck_sys_job_status CHECK (status IN ('0', '1')),
    CONSTRAINT ck_sys_job_del_flag CHECK (del_flag IN ('0', '1'))
);

CREATE TABLE sys_job_log (
    id BIGINT PRIMARY KEY,
    create_by VARCHAR(64),
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    job_id BIGINT,
    job_name VARCHAR(128),
    job_group VARCHAR(128),
    invoke_target VARCHAR(500),
    job_message VARCHAR(1000),
    status CHAR(1) NOT NULL DEFAULT '0',
    exception_info TEXT,
    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ,
    cost_time_ms BIGINT,
    CONSTRAINT ck_sys_job_log_status CHECK (status IN ('0', '1'))
);

CREATE UNIQUE INDEX uk_sys_user_keycloak_active ON sys_user (keycloak_user_id) WHERE del_flag = '0';
CREATE UNIQUE INDEX uk_sys_user_name_active ON sys_user (user_name) WHERE del_flag = '0';
CREATE INDEX idx_sys_user_dept_id ON sys_user (dept_id);
CREATE INDEX idx_sys_user_status ON sys_user (status);
CREATE INDEX idx_sys_user_del_flag ON sys_user (del_flag);

CREATE INDEX idx_sys_dept_parent_id ON sys_dept (parent_id);
CREATE INDEX idx_sys_dept_ancestors ON sys_dept (ancestors);
CREATE INDEX idx_sys_dept_status ON sys_dept (status);
CREATE INDEX idx_sys_dept_del_flag ON sys_dept (del_flag);

CREATE UNIQUE INDEX uk_sys_post_code_active ON sys_post (post_code) WHERE del_flag = '0';
CREATE INDEX idx_sys_post_status ON sys_post (status);
CREATE INDEX idx_sys_post_del_flag ON sys_post (del_flag);

CREATE INDEX idx_sys_menu_parent_id ON sys_menu (parent_id);
CREATE UNIQUE INDEX uk_sys_menu_permission_active ON sys_menu (permission_code) WHERE permission_code IS NOT NULL AND del_flag = '0';
CREATE INDEX idx_sys_menu_type ON sys_menu (menu_type);
CREATE INDEX idx_sys_menu_status ON sys_menu (status);
CREATE INDEX idx_sys_menu_del_flag ON sys_menu (del_flag);

CREATE UNIQUE INDEX uk_sys_role_key_active ON sys_role (role_key) WHERE del_flag = '0';
CREATE INDEX idx_sys_role_status ON sys_role (status);
CREATE INDEX idx_sys_role_del_flag ON sys_role (del_flag);

CREATE UNIQUE INDEX uk_sys_user_role ON sys_user_role (user_id, role_id);
CREATE INDEX idx_sys_user_role_role_id ON sys_user_role (role_id);

CREATE UNIQUE INDEX uk_sys_user_post ON sys_user_post (user_id, post_id);
CREATE INDEX idx_sys_user_post_post_id ON sys_user_post (post_id);

CREATE UNIQUE INDEX uk_sys_role_menu ON sys_role_menu (role_id, menu_id);
CREATE INDEX idx_sys_role_menu_menu_id ON sys_role_menu (menu_id);

CREATE UNIQUE INDEX uk_sys_role_dept ON sys_role_dept (role_id, dept_id);
CREATE INDEX idx_sys_role_dept_dept_id ON sys_role_dept (dept_id);

CREATE UNIQUE INDEX uk_sys_dict_type_active ON sys_dict_type (dict_type) WHERE del_flag = '0';
CREATE INDEX idx_sys_dict_type_status ON sys_dict_type (status);
CREATE INDEX idx_sys_dict_type_del_flag ON sys_dict_type (del_flag);

CREATE UNIQUE INDEX uk_sys_dict_data_value_active ON sys_dict_data (dict_type, dict_value) WHERE del_flag = '0';
CREATE INDEX idx_sys_dict_data_type ON sys_dict_data (dict_type);
CREATE INDEX idx_sys_dict_data_status ON sys_dict_data (status);
CREATE INDEX idx_sys_dict_data_del_flag ON sys_dict_data (del_flag);

CREATE UNIQUE INDEX uk_sys_config_key_active ON sys_config (config_key) WHERE del_flag = '0';
CREATE INDEX idx_sys_config_status ON sys_config (status);
CREATE INDEX idx_sys_config_del_flag ON sys_config (del_flag);

CREATE INDEX idx_sys_notice_type ON sys_notice (notice_type);
CREATE INDEX idx_sys_notice_status ON sys_notice (status);
CREATE INDEX idx_sys_notice_publish_time ON sys_notice (publish_time);
CREATE INDEX idx_sys_notice_del_flag ON sys_notice (del_flag);

CREATE INDEX idx_sys_oper_log_oper_time ON sys_oper_log (oper_time);
CREATE INDEX idx_sys_oper_log_user_id ON sys_oper_log (oper_user_id);
CREATE INDEX idx_sys_oper_log_business_type ON sys_oper_log (business_type);
CREATE INDEX idx_sys_oper_log_status ON sys_oper_log (status);
CREATE INDEX idx_sys_oper_log_trace_id ON sys_oper_log (trace_id);

CREATE INDEX idx_sys_login_log_login_time ON sys_login_log (login_time);
CREATE INDEX idx_sys_login_log_keycloak_user_id ON sys_login_log (keycloak_user_id);
CREATE INDEX idx_sys_login_log_user_name ON sys_login_log (user_name);
CREATE INDEX idx_sys_login_log_status ON sys_login_log (status);

CREATE UNIQUE INDEX uk_sys_job_name_group_active ON sys_job (job_name, job_group) WHERE del_flag = '0';
CREATE INDEX idx_sys_job_status ON sys_job (status);
CREATE INDEX idx_sys_job_del_flag ON sys_job (del_flag);

CREATE INDEX idx_sys_job_log_job_id ON sys_job_log (job_id);
CREATE INDEX idx_sys_job_log_start_time ON sys_job_log (start_time);
CREATE INDEX idx_sys_job_log_status ON sys_job_log (status);
CREATE INDEX idx_sys_job_log_name_group ON sys_job_log (job_name, job_group);

INSERT INTO sys_dept (
    id, parent_id, ancestors, dept_name, order_num, status, del_flag,
    create_by, create_time, remark
) VALUES (
    1000000000000000001, 0, '0', '系统管理', 1, '0', '0',
    '1000000000000000015', CURRENT_TIMESTAMP, '系统初始化部门'
);

INSERT INTO sys_role (
    id, role_name, role_key, role_sort, data_scope, menu_check_strictly,
    dept_check_strictly, status, del_flag, create_by, create_time, remark
) VALUES (
    1000000000000000002, '超级管理员', 'super_admin', 1, '1', 'Y',
    'Y', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统初始化超级管理员角色'
);

INSERT INTO sys_menu (
    id, menu_name, parent_id, order_num, path, component, route_name,
    is_frame, is_cache, menu_type, visible, status, permission_code,
    icon, del_flag, create_by, create_time, remark
) VALUES
    (
        1000000000000000003, '系统管理', 0, 1, 'system', NULL, 'System',
        'N', 'N', 'M', 'Y', '0', NULL, 'setting', '0',
        '1000000000000000015', CURRENT_TIMESTAMP, '系统管理目录'
    ),
    (
        1000000000000000004, '用户管理', 1000000000000000003, 1, 'user',
        'system/user/index', 'SystemUser', 'N', 'N', 'C', 'Y', '0',
        'system:user:list', 'user', '0', '1000000000000000015',
        CURRENT_TIMESTAMP, '用户管理菜单'
    ),
    (
        1000000000000000005, '角色管理', 1000000000000000003, 2, 'role',
        'system/role/index', 'SystemRole', 'N', 'N', 'C', 'Y', '0',
        'system:role:list', 'team', '0', '1000000000000000015',
        CURRENT_TIMESTAMP, '角色管理菜单'
    ),
    (
        1000000000000000006, '菜单管理', 1000000000000000003, 3, 'menu',
        'system/menu/index', 'SystemMenu', 'N', 'N', 'C', 'Y', '0',
        'system:menu:list', 'menu', '0', '1000000000000000015',
        CURRENT_TIMESTAMP, '菜单管理菜单'
    ),
    (
        1000000000000000007, '部门管理', 1000000000000000003, 4, 'dept',
        'system/dept/index', 'SystemDept', 'N', 'N', 'C', 'Y', '0',
        'system:dept:list', 'cluster', '0', '1000000000000000015',
        CURRENT_TIMESTAMP, '部门管理菜单'
    ),
    (
        1000000000000000008, '岗位管理', 1000000000000000003, 5, 'post',
        'system/post/index', 'SystemPost', 'N', 'N', 'C', 'Y', '0',
        'system:post:list', 'idcard', '0', '1000000000000000015',
        CURRENT_TIMESTAMP, '岗位管理菜单'
    );

INSERT INTO sys_role_menu (
    id, role_id, menu_id, create_by, create_time, remark
) VALUES
    (1000000000000000009, 1000000000000000002, 1000000000000000003, '1000000000000000015', CURRENT_TIMESTAMP, '超级管理员系统管理目录权限'),
    (1000000000000000010, 1000000000000000002, 1000000000000000004, '1000000000000000015', CURRENT_TIMESTAMP, '超级管理员用户管理权限'),
    (1000000000000000011, 1000000000000000002, 1000000000000000005, '1000000000000000015', CURRENT_TIMESTAMP, '超级管理员角色管理权限'),
    (1000000000000000012, 1000000000000000002, 1000000000000000006, '1000000000000000015', CURRENT_TIMESTAMP, '超级管理员菜单管理权限'),
    (1000000000000000013, 1000000000000000002, 1000000000000000007, '1000000000000000015', CURRENT_TIMESTAMP, '超级管理员部门管理权限'),
    (1000000000000000014, 1000000000000000002, 1000000000000000008, '1000000000000000015', CURRENT_TIMESTAMP, '超级管理员岗位管理权限');

INSERT INTO sys_user (
    id, keycloak_user_id, dept_id, user_name, nick_name, user_type,
    email, status, del_flag, create_by, create_time, remark
) VALUES (
    1000000000000000015, 'admin-keycloak-sub', 1000000000000000001,
    'admin', '系统管理员', 'SYSTEM', NULL, '0', '0',
    '1000000000000000015', CURRENT_TIMESTAMP, '默认管理员用户映射，不包含本地密码'
);

INSERT INTO sys_user_role (
    id, user_id, role_id, create_by, create_time, remark
) VALUES (
    1000000000000000016, 1000000000000000015, 1000000000000000002,
    '1000000000000000015', CURRENT_TIMESTAMP, '默认管理员绑定超级管理员角色'
);

INSERT INTO sys_post (
    id, post_code, post_name, post_sort, status, del_flag,
    create_by, create_time, remark
) VALUES (
    1000000000000000017, 'admin', '系统管理员', 1, '0', '0',
    '1000000000000000015', CURRENT_TIMESTAMP, '系统初始化岗位'
);

INSERT INTO sys_user_post (
    id, user_id, post_id, create_by, create_time, remark
) VALUES (
    1000000000000000018, 1000000000000000015, 1000000000000000017,
    '1000000000000000015', CURRENT_TIMESTAMP, '默认管理员绑定系统管理员岗位'
);

INSERT INTO sys_dict_type (
    id, dict_name, dict_type, status, del_flag, create_by, create_time, remark
) VALUES
    (1000000000000000019, '系统开关', 'sys_normal_disable', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典'),
    (1000000000000000020, '是否选项', 'sys_yes_no', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典'),
    (1000000000000000021, '用户性别', 'sys_user_sex', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典'),
    (1000000000000000022, '通知类型', 'sys_notice_type', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典'),
    (1000000000000000023, '任务状态', 'sys_job_status', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典');

INSERT INTO sys_dict_data (
    id, dict_sort, dict_label, dict_value, dict_type, list_class,
    is_default, status, del_flag, create_by, create_time, remark
) VALUES
    (1000000000000000024, 1, '正常', '0', 'sys_normal_disable', 'success', 'Y', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000025, 2, '停用', '1', 'sys_normal_disable', 'default', 'N', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000026, 1, '是', 'Y', 'sys_yes_no', 'success', 'N', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000027, 2, '否', 'N', 'sys_yes_no', 'default', 'Y', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000028, 1, '未知', '0', 'sys_user_sex', 'default', 'Y', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000029, 2, '男', '1', 'sys_user_sex', 'primary', 'N', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000030, 3, '女', '2', 'sys_user_sex', 'danger', 'N', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000031, 1, '通知', '1', 'sys_notice_type', 'primary', 'Y', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000032, 2, '公告', '2', 'sys_notice_type', 'success', 'N', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000033, 1, '正常', '0', 'sys_job_status', 'success', 'Y', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项'),
    (1000000000000000034, 2, '暂停', '1', 'sys_job_status', 'default', 'N', '0', '0', '1000000000000000015', CURRENT_TIMESTAMP, '系统内置字典项');

INSERT INTO sys_config (
    id, config_name, config_key, config_value, config_type, status,
    del_flag, create_by, create_time, remark
) VALUES (
    1000000000000000035, '日志默认保留天数', 'sys.log.retention.days',
    '180', 'Y', '0', '0', '1000000000000000015',
    CURRENT_TIMESTAMP, '系统管理域日志默认保留策略，单位：天'
);
