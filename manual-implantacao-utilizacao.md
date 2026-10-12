# Manual de implantação e utilização

## 1. Requisitos
- JDK 25
- Git (e acesso ao repositório)
- Internet na primeira execução (o Gradle baixa as dependências)
- Não é preciso instalar o Gradle: o projeto já inclui o Gradle Wrapper (`gradlew`)

## 2. Implantação

### 2.1 Obter o código
```
git clone https://github.com/victormunarim/fatura.git
cd fatura
```

### 2.2 Compilar e executar os testes
```
.\gradlew.bat build        (Windows)
./gradlew build            (Linux/macOS)
```

### 2.3 Executar a aplicação
**Opção A (IntelliJ):** abra a pasta do projeto, aguarde o Gradle sincronizar e execute
a classe `FaturaApplication` (botão Run).

**Opção B (terminal):**
```
.\gradlew.bat bootRun      (Windows)
./gradlew bootRun          (Linux/macOS)
```
A aplicação sobe em `http://localhost:8080`. No console aparece
`Started FaturaApplication in ... seconds`.

### 2.4 Configuração
- Arquivo: `src/main/resources/application.properties`.
- Banco: H2 **em memória** (`jdbc:h2:mem:fatura`, usuário `sa`, sem senha). Os dados são
  apagados sempre que a aplicação reinicia.
- Para mudar a porta, acrescente `server.port=8081` nesse arquivo.
- Arquivo processado: `fatura/itau_extrato.pdf` (caminho relativo à raiz do projeto;
  execute a aplicação a partir da raiz). Só o layout de extrato do Itaú do arquivo de
  exemplo é reconhecido (linhas no formato `dd/mm/aaaa DESCRIÇÃO valor`).

## 3. Utilização

Exemplos no PowerShell (`irm` = `Invoke-RestMethod`). No Linux/macOS use `curl`.
Não coloque barra `/` no final das URLs.

| O que fazer | Comando |
|---|---|
| 1. Processar o PDF e salvar as compras | `irm -Method POST http://localhost:8080/faturas` |
| Listar todas as compras | `irm http://localhost:8080/compras` |
| Listar da maior para a menor | `irm http://localhost:8080/compras/ordenado/decrescente` |
| Filtrar por categoria | `irm http://localhost:8080/compras/categoria/Pix` |
| Ver a distribuição dos gastos por categoria | `irm http://localhost:8080/compras/distribuicao` |
| Apagar todas as compras e faturas | `irm -Method DELETE http://localhost:8080/faturas/deleta` |

Exemplo de resposta da distribuição (os valores dependem do PDF):
```json
[
  { "categoria": "Pagamento de fatura", "total": 2351.54, "percentual": 65.38 },
  { "categoria": "Pix", "total": 1229.01, "percentual": 34.17 }
]
```
Erros: `404` quando não há dados para a consulta; `500` para erros inesperados. A lista
completa de endpoints está no `README.md`.

## 4. Executar os testes
- IntelliJ: clique direito em `src/test` → *Run Tests*.
- Terminal: `.\gradlew.bat test`

## 5. Banco de dados (console H2)
Com a aplicação rodando, abra `http://localhost:8080/h2-console` e use
JDBC URL `jdbc:h2:mem:fatura`, usuário `sa`, senha em branco.