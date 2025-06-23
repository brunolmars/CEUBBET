# Documentação da API CEUBET - Slot Machine

## 🎰 Visão Geral
CEUBET é um aplicativo de caça-níquel (slot machine) desenvolvido para Android, inspirado no estilo Starburst, com 5 roletas, efeitos visuais e sistema de áudio completo.

## 📱 Interface Principal (MainActivity)

### Atributos Principais

#### Elementos Visuais
- `reel1` até `reel5`: TextViews que mostram os símbolos principais das roletas
- `reel1Above` até `reel5Above`: TextViews que mostram os símbolos acima
- `reel1Below` até `reel5Below`: TextViews que mostram os símbolos abaixo
- `spinButton`: Botão para girar as roletas
- `creditsDisplay`: Mostra a quantidade de créditos do jogador
- `titleDisplay`: Mostra o título do jogo
- `messageDisplay`: Exibe mensagens para o jogador

#### Sistema de Créditos
- `currentCredits`: Começa com 100 créditos
- `SPIN_COST`: Custo de 5 créditos por jogada

#### Sistema de Sorte
- `consecutiveLosses`: Contador de perdas consecutivas
- `LUCK_THRESHOLD`: Limite de 5 perdas para ativar sistema de sorte

### 🎮 Funcionalidades Principais

#### `initializeViews()`
- Inicializa todos os elementos visuais
- Configura estilos e animações do botão de girar
- Define cores e efeitos visuais

#### `initSymbols()`
Configura os símbolos do jogo com suas características:
```
🟣 (Roxo)    - Peso: 2  - Multiplicador: 500x
🟢 (Verde)   - Peso: 5  - Multiplicador: 200x
🟡 (Amarelo) - Peso: 10 - Multiplicador: 100x
🔷 (Azul)    - Peso: 15 - Multiplicador: 50x
🔴 (Vermelho)- Peso: 18 - Multiplicador: 25x
⭐ (Estrela) - Peso: 20 - Multiplicador: 10x
✨ (Brilho)  - Peso: 30 - Multiplicador: 5x
```

#### `startSlotAnimation()`
- Inicia a animação das roletas
- Toca o som de giro
- Desabilita o botão durante o giro
- Controla a duração e velocidade de cada roleta

### 💰 Sistema de Prêmios

#### Combinações Vencedoras
1. **Mega Jackpot** (💎💎💎)
   - Prêmio: 500x a aposta
   - Animação especial com fogos de artifício

2. **Grande Prêmio** (🔮🔮🔮)
   - Prêmio: 200x a aposta
   - Animação de flash e tremor intenso

3. **Prêmio Médio** (💠💠💠)
   - Prêmio: 100x a aposta
   - Animação de tremor e pulso

4. **Prêmio Menor** (🌟🌟🌟)
   - Prêmio: 50x a aposta
   - Animação de pulso

5. **Mini Prêmios**
   - Duas Estrelas (⭐⭐): 2x a aposta
   - Três Brilhos (✨✨✨): 5x a aposta

### 🎵 Sistema de Áudio

#### `initializeAudioPlayers()`
Gerencia três tipos de sons:
- `spinning_counter.mp3`: Som de giro (loop)
- `prize_win.mp3`: Som de vitória
- `button_click.mp3`: Som do botão

### ✨ Efeitos Visuais

#### Animações de Vitória
- `showJackpotWin()`: Explosão de cores e fogos de artifício
- `showMajorWin()`: Flash e tremor intenso
- `showMediumWin()`: Tremor moderado
- `showMinorWin()`: Pulso suave
- `showMiniWin()`: Pequena animação de escala

#### Efeitos Especiais
- `flashScreen()`: Flash branco na tela
- `shakeScreen()`: Efeito de tremor
- `pulseSymbols()`: Símbolos pulsando
- `rotateWin()`: Rotação da tela
- `showFireworks()`: Efeito de fogos de artifício

### 🎲 Sistema de Sorte

O sistema aumenta as chances de vitória após 5 perdas consecutivas:
- Ativa símbolos especiais com maior chance de prêmios
- Mostra mensagens motivacionais
- 30% de chance de ativar roleta com maior probabilidade de ganhos

### 📱 Gestão do Ciclo de Vida

- `onPause()`: Pausa todos os sons
- `onResume()`: Retoma sons se necessário
- `onDestroy()`: Libera recursos de áudio e animações

## 🔧 Requisitos Técnicos
- Android SDK
- Suporte a animações
- Capacidade de reprodução de áudio
- Memória suficiente para efeitos visuais

## 📝 Observações
- O jogo é puramente demonstrativo
- Não envolve dinheiro real
- Focado em entretenimento e experiência do usuário 