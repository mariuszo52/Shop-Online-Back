insert into user_info (name, last_name) values ('mariusz','ozga');
INSERT INTO user_role (name) values ('ADMIN'), ('USER');
insert into users (username, email, password, user_role_id, user_info_id)
values ('mariuszo', 'mariuszo8@vp.pl','{bcrypt}$2a$10$K9bRFxj3dvfQFQQ6hl.wuua.FRiOQhZNKbn3tvC2Od6c8kvohEp7e', 2, 1);
INSERT INTO Genre (name) VALUES
                              ('Action'),
                              ('Adventure'),
                              ('Casual'),
                              ('Co-op'),
                              ('FPS'),
                              ('Fighting'),
                              ('Hack and Slash'),
                              ('Horror'),
                              ('Indie'),
                              ('Life Simulation'),
                              ('Open World'),
                              ('Platformer'),
                              ('Point & click'),
                              ('Puzzle'),
                              ('RPG'),
                              ('Racing'),
                              ('Simulation'),
                              ('Sport'),
                              ('Story rich'),
                              ('Strategy');
INSERT INTO language (name, icon_url)
VALUES
    ('Polish', 'https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Flag_of_Poland.svg/1280px-Flag_of_Poland.svg.png'),
    ('German', 'https://upload.wikimedia.org/wikipedia/en/thumb/b/ba/Flag_of_Germany.svg/1920px-Flag_of_Germany.svg.png'),
    ('French', 'https://upload.wikimedia.org/wikipedia/en/thumb/c/c3/Flag_of_France.svg/1920px-Flag_of_France.svg.png'),
    ('Italian', 'https://upload.wikimedia.org/wikipedia/en/thumb/0/03/Flag_of_Italy.svg/1920px-Flag_of_Italy.svg.png'),
    ('Spanish', 'https://upload.wikimedia.org/wikipedia/en/thumb/9/9a/Flag_of_Spain.svg/1920px-Flag_of_Spain.svg.png'),
    ('Portuguese', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flag_of_Portugal.svg/1920px-Flag_of_Portugal.svg.png'),
    ('English', 'https://upload.wikimedia.org/wikipedia/en/thumb/a/ae/Flag_of_the_United_Kingdom.svg/1920px-Flag_of_the_United_Kingdom.svg.png'),
    ('Swedish', 'https://upload.wikimedia.org/wikipedia/en/thumb/4/4c/Flag_of_Sweden.svg/1280px-Flag_of_Sweden.svg.png'),
    ('Norwegian', 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Flag_of_Norway.svg/1920px-Flag_of_Norway.svg.png'),
    ('Danish', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Flag_of_Denmark.svg/1920px-Flag_of_Denmark.svg.png'),
    ('Finnish', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Flag_of_Finland.svg/1920px-Flag_of_Finland.svg.png'),
    ('Greek', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flag_of_Greece.svg/1920px-Flag_of_Greece.svg.png'),
    ('Swiss', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Flag_of_Switzerland.svg/1920px-Flag_of_Switzerland.svg.png'),
    ('Austrian', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Flag_of_Austria.svg/1920px-Flag_of_Austria.svg.png'),
    ('Dutch', 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/20/Flag_of_the_Netherlands.svg/1920px-Flag_of_the_Netherlands.svg.png'),
    ('Belgian', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Flag_of_Belgium_%28civil%29.svg/1920px-Flag_of_Belgium_%28civil%29.svg.png'),
    ('Russian', 'https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/1920px-Flag_of_Russia.svg.png'),
    ('Japanese', 'https://upload.wikimedia.org/wikipedia/en/thumb/9/9e/Flag_of_Japan.svg/1920px-Flag_of_Japan.svg.png'),
    ('Chinese', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Flag_of_the_People%27s_Republic_of_China.svg/1920px-Flag_of_the_People%27s_Republic_of_China.svg.png'),
    ('Korean', 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/Flag_of_South_Korea.svg/1920px-Flag_of_South_Korea.svg.png'),
    ('Arabic', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flag_of_the_Arab_League.svg/1920px-Flag_of_the_Arab_League.svg.png'),
    ('Turkish', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Flag_of_Turkey.svg/1920px-Flag_of_Turkey.svg.png'),
    ('Hindi', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Flag_of_India.svg/1920px-Flag_of_India.svg.png'),
    ('Urdu', 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/32/Flag_of_Pakistan.svg/1920px-Flag_of_Pakistan.svg.png'),
    ('Bengali', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f9/Flag_of_Bangladesh.svg/1920px-Flag_of_Bangladesh.svg.png'),
    ('Persian', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Flag_of_Iran.svg/1920px-Flag_of_Iran.svg.png'),
    ('Czech', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Flag_of_the_Czech_Republic.svg/1280px-Flag_of_the_Czech_Republic.svg.png'),
    ('Portuguese - Brazil', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flag_of_Portugal.svg/1920px-Flag_of_Portugal.svg.png'),
    ('Hungarian', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Flag_of_Hungary.svg/1920px-Flag_of_Hungary.svg.png'),
    ('Thai', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Flag_of_Thailand.svg/1280px-Flag_of_Thailand.svg.png'),
    ('Romanian', 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Flag_of_Romania.svg/1280px-Flag_of_Romania.svg.png'),
    ('Bulgarian', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Flag_of_Bulgaria.svg/1920px-Flag_of_Bulgaria.svg.png'),
    ('Ukrainian', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Flag_of_Ukraine.svg/1280px-Flag_of_Ukraine.svg.png');

