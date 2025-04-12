
### 📁 `domain/`
É o **coração da regra de negócio**. Nada aqui depende das outras camadas. Essa camada é pura e reutilizável.

- **`model/`**  
  Contém os modelos puros (entidades) usados pela lógica de negócio.

- **`usecase/`**  
  Cada arquivo define uma ação específica que o app pode executar (ex: `LoginUseCase`, `GetUserProfileUseCase`). É aqui que a lógica de negócio acontece.

- **`repository/`**  
  Interfaces dos repositórios. Servem como ponte para a camada `data`, mas não sabem de onde os dados vêm.

---