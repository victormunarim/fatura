# Fatura

Sistema backend para upload, processamento e organização de faturas de cartão de crédito desenvolvido para web com o objetivo de melhorar a organização financeira

## Público alvo

Pessoas que procuram organizar os gastos que tem na fatura de uma forma a investir ou economizar dinheiro.
Além de visualizar de forma personalizavel.

## Requisitos funcionais

* Deve ser possível ordenar os valores; Ex: Usuário escolhe critério de ordenação (crescente/decrescente) → sistema reordena a lista pelo valor.
* Deve ter filtros de pesquisa para agrupar as compras por categoria ou descrição; Ex: Usuário seleciona uma categoria ou descrição → sistema exibe apenas os lançamentos correspondentes.
* Upload de arquivos de fatura em formato PDF; Ex: Usuário seleciona um arquivo PDF → sistema recebe o arquivo.
* Validação do tipo e formato do arquivo; Ex: Sistema verifica se a extensão é .pdf e se o tamanho está dentro do limite → aprova o arquivo para processamento.
* Processamento automático da fatura após o upload; Ex: Sistema lê o PDF, extrai data, descrição, valor e parcela de cada lançamento, e os disponibiliza para listagem.

## Requisitos não funcionais

* Desempenho: Processar faturas de até um tamanho específico (a definir).
* Confiabilidade: Falha na extração da fatura não compromete o resto da aplicação.
* Compatibilidade: suporte para fatura de 1 banco específico (a definir).

## Modelagem

O sistema tera uma interface simples, somente para upload de arquivos e listagem na web.
Os gastos processados deverão ser disponibilizados de forma estruturada.

## Stacks

* Spring framework;
* Java 25;
* Banco H2;
