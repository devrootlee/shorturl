DROP TABLE IF EXISTS Member;

create table Member
(
    id integer not null,
    name varchar(255) not null,
    primary key (id)
);

-- origin_url 에도 unique를 걸어두려했으나
-- 다른 상용 DB에서는 TEXT type에 UNIQUE 제약을 걸면 길이 제한이 필요 -> 전체 인덱싱이 안되므로 코드단에서 처리
create table Url
(
    id integer auto_increment not null,
    ori_url text not null,
    short_url varchar(8) not null UNIQUE,
    request_count integer not null default 0,
    primary key (id)
);