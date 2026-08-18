# Fatura

Sistema backend para upload, processamento e organização de faturas de cartão de crédito.

O sistema permite que o usuário envie uma fatura em PDF, extraia automaticamente os lançamentos, organize os gastos por categoria e disponibilize os dados para consulta e exportação.

O projeto tem como objetivo desenvolver uma API capaz de transformar uma fatura de cartão de crédito em dados estruturados, facilitando a visualização e a análise dos gastos.

## Funcionalidades

### Upload de fatura

* Upload de arquivos de fatura em formato PDF.
* Validação do tipo e formato do arquivo.
* Processamento automático da fatura após o upload.

### Leitura da fatura

O sistema deverá ser capaz de interpretar uma fatura PDF padrão de um banco e extrair informações como:

* Data da compra;
* Descrição;
* Valor da compra;
* Número da parcela, quando aplicável;
* Categoria do gasto.

### Organização dos gastos

Os lançamentos extraídos deverão ser organizados em categorias, permitindo uma melhor visualização dos gastos.

### Visualização

Os gastos processados deverão ser disponibilizados de forma estruturada, permitindo:

* Listar todos os lançamentos;
* Consultar gastos por categoria;
* Consultar gastos por período;
* Visualizar o valor total da fatura;
* Visualizar o total gasto em cada categoria.
