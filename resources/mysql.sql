CREATE DATABASE IF NOT EXISTS practica2_3ud;
__
USE practica2_3ud;
__
CREATE TABLE IF NOT EXISTS producto (
id_producto  int auto_increment primary key,
nombre varchar(50) not null,
descripcion varchar(200) not null,
estado varchar(50) not null,
modelo varchar(50) not null,
marca varchar(50) not null
);
__
CREATE TABLE IF NOT EXISTS empresa (
id_empresa int auto_increment primary key,
nombre varchar(50) not null,
descripcion varchar(200) not null,
fecha_de_creacion date,
ubicacion varchar (100) not null,
valoracion int not null
);
__
CREATE TABLE IF NOT EXISTS kit_educativo (
id_kit int auto_increment primary key,
nombre varchar(50) not null,
descripcion varchar(200) not null,
cantidad int not null,
fecha_de_creacion date,
fecha_de_actualizacion date,
id_producto int not null,
id_empresa int not null,
precio decimal (12,2)not null,
valoracion int not null
);
__
alter table kit_educativo
add foreign key (id_producto) references producto(id_producto),
add foreign key (id_empresa) references empresa(id_empresa);