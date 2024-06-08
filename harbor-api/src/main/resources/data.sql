insert into prestador
    (nome, email, senha)
values
    ('John Doe', 'john@doe.com', '$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC');


insert into cargo (nome_cargo) values ('Gestor'), ('Prestador'), ('Atendente');

INSERT INTO endereco (bairro, logradouro, cidade, estado, cep, complemento) VALUES
('Vila Nova Cachoeirinha', 'Avenida Deputado Emílio Carlos', 'São Paulo', 'SP', '02611000', 'Apto 101'),
('Centro', 'Rua Direita', 'Rio de Janeiro', 'RJ', '20010020', 'Sala 202'),
('Boa Viagem', 'Avenida Boa Viagem', 'Recife', 'PE', '51021000', 'Casa 3'),
('Pituba', 'Avenida Manoel Dias da Silva', 'Salvador', 'BA', '41830001', 'Bloco B'),
('Centro', 'Rua XV de Novembro', 'Curitiba', 'PR', '80020310', 'Loja 1'),
('Boa Vista', 'Avenida Rio Branco', 'Boa Vista', 'RR', '69301110', 'Apto 20'),
('Santo Amaro', 'Avenida Adolfo Pinheiro', 'São Paulo', 'SP', '04733100', 'Casa 2'),
('Ipanema', 'Rua Visconde de Pirajá', 'Rio de Janeiro', 'RJ', '22410003', 'Apartamento 401'),
('Ponta Negra', 'Avenida Engenheiro Roberto Freire', 'Natal', 'RN', '59090000', 'Casa 5'),
('Centro', 'Avenida Paulista', 'São Paulo', 'SP', '01310000', 'Andar 15');

INSERT INTO cliente
(nome, sobrenome, telefone, cpf)
VALUES
    ('João', 'Silva', '123456789', '80662680090');