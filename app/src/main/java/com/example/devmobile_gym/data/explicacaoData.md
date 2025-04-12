
### 📁 `data/`
Responsável por obter os dados brutos (seja de rede, banco local, etc.). Aqui ficam as **implementações concretas** das interfaces da `domain`.

- **`remote/`**  
  Contém as fontes de dados externas, como chamadas HTTP, APIs com Retrofit, Firebase, etc.

- **`local/`**  
  Fontes de dados internas, como banco de dados local (Room), DataStore, SharedPreferences, etc.

- **`dto/` (Data Transfer Object)**  
  Representações dos dados que vêm da rede. São transformados em `model` para uso interno da app.

- **`repository/`**  
  Implementações concretas das interfaces definidas na camada `domain.repository`. Aqui você conecta os dados da `remote` e/ou `local`.

  

### 📁 `di/` (Dependency Injection)
Aqui ficam os módulos do Hilt ou Koin. Essa pasta cuida da **injeção de dependências**, ou seja, liga interfaces às implementações.

