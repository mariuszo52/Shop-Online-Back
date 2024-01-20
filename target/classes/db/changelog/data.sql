insert into shipping_address (address, city, country, postal_code, phone_number) values ('Krakowska 1', 'Cracow', 'Poland', '30003', '123456789');
insert into shipping_address (address, city, country, postal_code, phone_number) values ('Batorego 56', 'Nowy Sącz', 'Poland', '33300', '987654321');
insert into user_info (name, last_name, shipping_address_id) values ('mariusz','ozga', 1);
insert into user_info (name, last_name, shipping_address_id) values ('adminName','adminLastName', 2);
INSERT INTO user_role (name) values ('ADMIN'), ('USER');
insert into cart () values ();
insert into cart () values ();
insert into users (username, email, password, user_role_id, user_info_id, is_enabled, cart_id, type)
values ('mariuszo', 'mariuszo8@vp.pl','{bcrypt}$2a$10$K9bRFxj3dvfQFQQ6hl.wuua.FRiOQhZNKbn3tvC2Od6c8kvohEp7e', 2, 1, true, 1, 'STANDARD');
insert into users (username, email, password, user_role_id, user_info_id, is_enabled, cart_id, type)
values ('admin', 'kontakt@mowebcreations.pl','{bcrypt}$2a$10$K9bRFxj3dvfQFQQ6hl.wuua.FRiOQhZNKbn3tvC2Od6c8kvohEp7e', 1, 2, true, 2, 'STANDARD');