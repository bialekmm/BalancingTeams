package com.github.bialekmm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Individual> individuals = new ArrayList<>();
        individuals.add(new Individual("Johnny",8));
        individuals.add(new Individual("Robbie",5));
        individuals.add(new Individual("Juliet",3));
        individuals.add(new Individual("Scarlet",5));
        individuals.add(new Individual("Jude",9));
        individuals.add(new Individual("Deborah",6));

        int teamsNum = 3;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        List<List<Individual>> teams = balancingGenetic(individuals, teamsNum, 1000);

        for (int i = 0; i < teams.size(); i++) {
            String players = teams.get(i).stream().map(Individual::getName).collect(Collectors.joining(", "));
            double aveRate = (double)(teams.get(i).stream().mapToInt(Individual::getRate).sum())/teams.get(i).size();
            System.out.println("Team no " + (i+1) + " has " + teams.get(i).size() + " players (" + players + "). Average rate: " + aveRate);
        }
        System.out.println("Teams rate standard deviation: " + decimalFormat.format(deviation(teams, individuals)));
    }

    static List<List<Individual>> balancingGenetic(List<Individual> individuals, int teamsNum, int generations) {
        List<List<Individual>> bestTeams = null;
        double minDifference = Double.MAX_VALUE;

        for (int gen = 0; gen < generations; gen++) {
            List<List<Individual>> teams = generateRandomTeams(individuals, teamsNum);
            double maxTeamAvg = teams.stream()
                    .mapToDouble(team -> team.stream().mapToInt(Individual::getRate).average().orElse(0))
                    .max().orElse(0);
            double minTeamAvg = teams.stream()
                    .mapToDouble(team -> team.stream().mapToInt(Individual::getRate).average().orElse(0))
                    .min().orElse(0);
            double difference = maxTeamAvg - minTeamAvg;

            if (difference < minDifference) {
                bestTeams = teams;
                minDifference = difference;
            }
        }

        return bestTeams;
    }

    static List<List<Individual>> generateRandomTeams(List<Individual> individuals, int teamsNum) {
        List<Individual> individualsCopy = new ArrayList<>(individuals);
        Collections.shuffle(individualsCopy);

        List<List<Individual>> teams = new ArrayList<>();
        for (int i = 0; i < teamsNum; i++) {
            teams.add(new ArrayList<>());
        }

        for (int i = 0; i < individualsCopy.size(); i++) {
            teams.get(i % teamsNum).add(individualsCopy.get(i));
        }

        teams.removeIf(List::isEmpty);
        return teams;
    }

    static double deviation(List<List<Individual>> teams, List<Individual> individuals) {
        double mean = (double) (individuals.stream().mapToInt(Individual::getRate).sum())/individuals.size();
        double variance = 0.00;
        for (List<Individual> team : teams) {
            variance += Math.pow(((double) team.stream().mapToInt(Individual::getRate).sum()/team.size()) - mean, 2);
        }
        return Math.sqrt(variance/teams.size());
    }
}
