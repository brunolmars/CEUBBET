package com.example.ceubetjava.blackjack.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.ceubetjava.R;

/**
 * Activity que exibe as regras do jogo de Blackjack
 */
public class RulesActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        
        // Inicializa as views
        TextView tvRules = findViewById(R.id.tv_rules);
        
        // Carrega as regras
        String rules = loadRules();
        
        // Exibe as regras
        tvRules.setText(HtmlCompat.fromHtml(rules, HtmlCompat.FROM_HTML_MODE_COMPACT));
    }
    
    /**
     * Carrega as regras do jogo
     * @return regras formatadas em HTML
     */
    private String loadRules() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<h1>Regras do Blackjack</h1>");
        sb.append("<p>O BlackJack | 21, é o mais famoso jogo de cartas dos Casinos em todo o mundo, jogado com 4 ou 6 baralhos de 52 cartas. O objetivo do jogo é tentar alcançar a combinação BlackJack (quando as 2 primeiras cartas distribuídas, forem um Ás e a outra uma figura ou um 10) ou chegar à pontuação de 21 sem a ultrapassar.</p>");
        
        sb.append("<h2>Apostas</h2>");
        sb.append("<p>O jogador aposta antes de se iniciar a distribuição das cartas, entre o mínimo fixado, ou múltiplos dele até ao máximo permitido, e poderá jogar em mais de uma casa. São distribuídas, alternadamente, 2 cartas por jogador e 2 para a banca (a 2ª carta da banca fica virada para baixo, pelo que o seu valor é incógnito). As cartas com figuras valem 10 pontos, o Ás vale 1 ou 11, conforme opção do jogador, e as restantes cartas valem o seu valor facial.</p>");
        
        sb.append("<p>Se as 2 cartas que o jogador recebeu inicialmente formarem um BlackJack, a jogada está ganha, a não ser que a banca também faça BlackJack. Nesse caso dá-se uma situação de empate perdendo apenas os jogadores que não tenham BlackJack.</p>");
        
        sb.append("<p>O jogador que não tiver BlackJack pode continuar a pedir cartas para tentar chegar o mais perto de 21 sem ultrapassar, e poderá parar sempre que o pretender, desde que tenha uma pontuação superior a 11.</p>");
        
        sb.append("<p>Posteriormente a banca mostra a sua carta escondida e soma os seus pontos. A soma das cartas da banca tem de ser no mínimo 17, parando de receber cartas ao atingir este valor. Quando a banca ultrapassa os 21, perde imediatamente e ganham todos os jogadores em jogo.</p>");
        
        sb.append("<h2>Desistência</h2>");
        sb.append("<p>Após a distribuição das 2 primeiras cartas ao jogador e à banca, o jogador pode desistir da jogada, perdendo metade da importância apostada, desde que a carta da banca não seja um Ás.</p>");
        
        sb.append("<h2>Seguro</h2>");
        sb.append("<p>Quando a carta aberta da banca for um Ás, os jogadores poderão fazer seguro da sua aposta, colocando, no lugar demarcado no pano da banca, fichas cuja importância total não exceda metade do valor da aposta inicialmente feita. Se a banca vier a fazer Blackjack, a aposta feita no seguro ganhará o dobro do seu valor, sendo perdida em caso contrário.</p>");
        
        sb.append("<h2>Aposta Dupla</h2>");
        sb.append("<p>Se, nas 2 primeiras cartas de cada aposta, o jogador totalizar 9, 10 ou 11, poderá duplicar o valor da aposta inicial, sendo-lhe distribuída apenas uma única carta.</p>");
        
        sb.append("<h2>Pares</h2>");
        sb.append("<p>Sempre que as 2 primeiras cartas distribuídas a um jogador, ainda que de naipes diferentes, tenham o mesmo valor, este poderá separá-las e fazer 2 apostas independentes, desde que jogue em cada uma delas importância igual à da parada inicial. Se o par desdobrado for de Ases apenas poderá pedir uma carta para cada aposta. Depois de efetuado o desdobramento do par inicial, poderá voltar a fazê-lo, se vier a obter uma ou mais cartas adicionais do mesmo valor.</p>");
        
        sb.append("<h2>Prêmios</h2>");
        sb.append("<p>No caso de o jogador ganhar à banca sem BlackJack, ganha o valor igual ao da aposta, se ganhar à banca com BlackJack ganha 1,5 vezes o valor da aposta.</p>");
        
        sb.append("<h2>Prêmio Especial</h2>");
        sb.append("<p>Caso o jogador obtenha cartas com os valores \"6-7-8\" do mesmo naipe, ou \"7-7-7\", recebe de imediato 3 vezes o valor da sua aposta, permanecendo em jogo.</p>");
        
        return sb.toString();
    }
}
