package com.example.devmobile_gym.presentation.navigation

object AlunoRoutes {
    const val Home = "aluno/home"
    const val DetalhesTreino = "aluno/detalhesTreino/{treinoId}"
    const val ConcluirTreino = "aluno/concluirTreino/{treinoId}"
    const val Aulas = "aluno/aulas"
    const val Chatbot = "aluno/chatbot"
    const val Historico = "aluno/historico"
    const val Profile = "aluno/profile"
    const val Search = "aluno/search"
    const val QrCode = "aluno/qrcode"
}

object ProfessorRoutes {
    const val Home = "professor/home"
    const val DetalhesAluno = "professor/detalhesAluno/{alunoId}"
}

object AuthRoutes {
    const val Login = "login"
    const val Register = "register"
    const val Register2 = "register2"
}
