package com.example.ceubetjava.slot77.model;

import java.util.Arrays;

/**
 * Gerencia a tabela de prêmios do caça-níquel
 */
public class PayTable {
    private static final SlotSymbol[] SYMBOLS = {
            new SlotSymbol("💎", 2, 500, android.R.color.holo_purple), // Roxo
            new SlotSymbol("🔮", 5, 200, android.R.color.holo_blue_light), // Azul claro
            new SlotSymbol("💠", 10, 100, android.R.color.holo_green_light), // Verde
            new SlotSymbol("🌟", 15, 50, android.R.color.holo_orange_light), // Laranja
            new SlotSymbol("♦️", 18, 25, android.R.color.holo_red_light), // Vermelho
            new SlotSymbol("⭐", 20, 10, android.R.color.white), // Branco
            new SlotSymbol("✨", 30, 5, android.R.color.darker_gray) // Cinza
    };

    private static final int TOTAL_WEIGHT = Arrays.stream(SYMBOLS)
            .mapToInt(SlotSymbol::getWeight)
            .sum();

    public static SlotSymbol[] getSymbols() {
        return SYMBOLS;
    }

    public static int getTotalWeight() {
        return TOTAL_WEIGHT;
    }

    /**
     * Calcula o prêmio baseado nos símbolos da linha
     * @param symbols array com os símbolos sorteados
     * @param bet valor da aposta
     * @return valor do prêmio (0 se não houver)
     */
    public static long calculatePrize(String[] symbols, long bet) {
        if (symbols == null || symbols.length < 3) return 0;

        // Verifica combinações de 3 ou mais símbolos iguais
        if (symbols[0].equals("💎") && symbols[1].equals("💎") && symbols[2].equals("💎")) {
            return bet * 500; // MEGA JACKPOT
        }
        if (symbols[0].equals("🔮") && symbols[1].equals("🔮") && symbols[2].equals("🔮")) {
            return bet * 200; // GRANDE PRÊMIO
        }
        if (symbols[0].equals("💠") && symbols[1].equals("💠") && symbols[2].equals("💠")) {
            return bet * 100; // PRÊMIO MÉDIO
        }
        if (symbols[0].equals("🌟") && symbols[1].equals("🌟") && symbols[2].equals("🌟")) {
            return bet * 50; // PRÊMIO MENOR
        }
        if (symbols[0].equals("♦️") && symbols[1].equals("♦️") && symbols[2].equals("♦️")) {
            return bet * 25; // PRÊMIO DOS DIAMANTES
        }
        if (symbols[0].equals("⭐") && symbols[1].equals("⭐") && symbols[2].equals("⭐")) {
            return bet * 10; // PRÊMIO DAS ESTRELAS
        }
        if (symbols[0].equals("✨") && symbols[1].equals("✨") && symbols[2].equals("✨")) {
            return bet * 5; // PRÊMIO DOS BRILHOS
        }

        // Verifica combinação de 2 estrelas
        if ((symbols[0].equals("⭐") && symbols[1].equals("⭐")) ||
            (symbols[1].equals("⭐") && symbols[2].equals("⭐")) ||
            (symbols[0].equals("⭐") && symbols[2].equals("⭐"))) {
            return bet * 2; // PRÊMIO PEQUENO
        }

        return 0; // Sem prêmio
    }

    /**
     * Retorna a mensagem apropriada para o prêmio
     * @param prize valor do prêmio
     * @return mensagem formatada
     */
    public static String getPrizeMessage(long prize, long bet) {
        if (prize <= 0) return "❌ Não foi dessa vez! Tente novamente! ❌";
        
        if (prize == bet * 500) return "🎰 MEGA JACKPOT!!! 💎💎💎 !!! 🎰";
        if (prize == bet * 200) return "🎰 GRANDE PRÊMIO! CRISTAL MÁGICO! 🔮🔮🔮";
        if (prize == bet * 100) return "🎰 PRÊMIO MÉDIO! DIAMANTES AZUIS! 💠💠💠";
        if (prize == bet * 50) return "🎰 PRÊMIO! ESTRELAS! 🌟🌟🌟";
        if (prize == bet * 25) return "🎰 PRÊMIO DOS DIAMANTES! ♦️♦️♦️";
        if (prize == bet * 10) return "⭐ PRÊMIO DAS ESTRELAS! ⭐";
        if (prize == bet * 5) return "✨ PRÊMIO DOS BRILHOS! ✨";
        if (prize == bet * 2) return "⭐ Duas Estrelas! Pequeno Prêmio! ⭐";
        
        return "🎰 PRÊMIO! " + prize + " CRÉDITOS! 🎰";
    }
} 