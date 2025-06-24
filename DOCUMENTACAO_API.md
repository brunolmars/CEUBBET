Regras de Negócio: Aplicativo de Apostas 
Módulo 1: Gestão de Contas e Acesso
RN001 (Cadastro Obrigatório): Para salvar o progresso (pontos) e participar do ranking, o usuário deve criar uma conta.
RN002 (Dados de Cadastro): O formulário de cadastro deve solicitar obrigatoriamente:
Nome de Usuário (deve ser único no sistema)
E-mail (deve ser único e ter um formato válido)
Senha
Confirmação de Senha
RN003 (Validação de Senha): O campo "Senha" e "Confirmação de Senha" devem ser idênticos para que o cadastro seja efetuado.
RN004 (Complexidade da Senha): A senha deve ter no mínimo 6 caracteres para ser considerada válida.
RN005 (Login): Um usuário cadastrado pode acessar o sistema utilizando seu Nome de Usuário e Senha.
RN006 (Modo Convidado): O aplicativo deve oferecer uma opção "Entrar como Convidado" para usuários que não desejam se cadastrar.
RN007 (Limitações do Convidado): Contas de convidado não têm seus pontos salvos após fechar o aplicativo e não são elegíveis para o ranking de jogadores.
RN008 (Persistência de Dados): Todos os pontos e o histórico de um jogador cadastrado devem ser salvos e vinculados à sua conta.
Módulo 2: Moeda Virtual (Pontos) e Transações
RN009 (Moeda do Jogo): Todas as apostas, ganhos e perdas dentro do aplicativo são feitos com Pontos.
RN010 (Saldo Inicial - Jogador Cadastrado): Ao se cadastrar com sucesso, o jogador recebe um saldo inicial de 1.000 Pontos como bônus de boas-vindas.
RN011 (Saldo Insuficiente): Um jogador não pode realizar uma aposta com um valor superior ao seu saldo de Pontos atual.
Módulo 3: Jogabilidade
RN012 (Tela de Seleção): Após o login (ou entrada como convidado), o jogador é direcionado a uma tela onde pode escolher um dos três jogos: Caça-Níquel 777, Batalha Naval ou Blackjack.
RN013 (Processo de Aposta): Antes de iniciar cada rodada/partida, o jogador deve definir o valor em Pontos que deseja apostar.
RN014 (Débito da Aposta): O valor da aposta é debitado do saldo do jogador no momento em que a rodada/partida se inicia.
RN015 (Crédito do Prêmio): Em caso de vitória, os Pontos ganhos são creditados imediatamente ao saldo do jogador ao final da rodada/partida.
RN016 (Regra do Jogo - Caça-Níquel 777):
Uma aposta ativa um único giro.
Os prêmios são pagos com base em uma tabela de combinações pré-definida.
RN017 (Regra do Jogo - Batalha Naval):
O jogador aposta um valor fixo na vitória da partida.
Se o jogador vencer, ele recebe de volta o valor apostado mais um prêmio (ex: 2x o valor da aposta). Se perder, perde o valor apostado.
RN018 (Regra do Jogo - Blackjack):
O jogo segue as regras padrão do Blackjack contra um "dealer".
Vitória normal (pontuação maior que o dealer sem estourar 21) paga 1:1.
Em caso de empate ("Push"), o valor da aposta é devolvido ao jogador.
Módulo 4: Ranking
RN019 (Elegibilidade do Ranking): Apenas jogadores cadastrados são exibidos no ranking.
RN020 (Critério de Classificação): A posição no ranking é determinada pelo maior saldo total de Pontos.
RN021 (Critério de Desempate): Em caso de empate no número de pontos, o jogador que atingiu aquela pontuação primeiro terá a posição mais alta.
RN022 (Atualização do Ranking): O ranking deve ser atualizado em tempo real.
Módulo 5: Administração
RN023 (Acesso do Administrador): Administradores devem ter uma interface de login e um painel de controle separados e seguros, inacessíveis para jogadores.
RN024 (Gestão de Jogadores): O administrador pode visualizar a lista de todos os jogadores cadastrados, buscar por nome de usuário ou e-mail e ver seus saldos.
RN025 (Moderação): O administrador pode suspender ou banir a conta de um jogador em caso de violação dos termos de uso.
RN026 (Gestão de Pontos): O administrador pode creditar ou debitar Pontos manualmente da conta de um jogador (para fins de suporte, correção de erros ou promoções especiais).
RN027 (Configuração dos Jogos): O administrador pode ajustar parâmetros dos jogos, como o valor do bônus inicial, bônus diário, taxas de pagamento do Caça-Níquel e apostas mínimas/máximas.