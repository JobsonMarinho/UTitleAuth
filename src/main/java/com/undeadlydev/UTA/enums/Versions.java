package com.undeadlydev.UTA.enums;

import org.bukkit.Bukkit;

public enum Versions {
    v1_5(10500),
    v1_6(10600),
    v1_7(10700),
    v1_8_8(10808),
    v1_8_9(10809),
    v1_9(10900),
    v1_9_1(10901),
    v1_9_2(10902),
    v1_9_3(10903),
    v1_9_4(10904),
    v1_9_x(10910),
    v1_10(11000),
    v1_10_1(11001),
    v1_10_2(11002),
    v1_10_x(11010),
    v1_11(11100),
    v1_11_1(11101),
    v1_11_2(11102),
    v1_11_x(11110),
    v1_12(11200),
    v1_12_1(11201),
    v1_12_2(11202),
    v1_12_x(11210),
    v1_13(11300),
    v1_13_1(11301),
    v1_13_2(11302),
    v1_13_x(11310),
    v1_14(11400),
    v1_14_1(11401),
    v1_14_2(11402),
    v1_14_3(11403),
    v1_14_4(11404),
    v1_14_x(11410),
    v1_15(11500),
    v1_15_1(11501),
    v1_15_2(11502),
    v1_15_x(11510),
    v1_16(11600),
    v1_16_1(11601),
    v1_16_2(11602),
    v1_16_3(11603),
    v1_16_4(11604),
    v1_16_5(11605),
    v1_16_x(11610),
    v1_17(11700),
    v1_17_1(11701),

    v1_17_x(11710),

    v1_18(11800),
    v1_18_1(11801),
    v1_18_2(11802),
    v1_18_x(11810),

    v1_19(11900),
    v1_19_1(11901),
    v1_19_2(11902),
    v1_19_3(11903),
    v1_19_4(11904),

    v1_19_x(11910),

    v1_20(12000),
    v1_20_1(12001),
    v1_20_2(12002),
    v1_20_3(12003),
    v1_20_4(12004),
    v1_20_5(12005),
    v1_20_6(12006),

    v1_20_x(12010),

    v1_21(12100),
    v1_21_1(12101),
    v1_21_2(12102),
    v1_21_3(12103),
    v1_21_4(12104),
    v1_21_5(12105),
    v1_21_6(12106),
    v1_21_7(12107),
    v1_21_8(12108),
    v1_21_9(12109),
    v1_21_10(12110),
    v1_21_11(12111),

    v1_21_x(12120),

    // Year-based scheme introduced in 2026 (e.g. "26.1.2" -> year 26, drop 1, hotfix 2).
    // Value formula keeps ordering: major*10000 + minor*100 + patch.
    v26(260000),
    v26_1(260100),
    v26_1_1(260101),
    v26_1_2(260102),

    v26_1_x(260110),

    vUnsupported(1000000);

    private int value;
    private boolean contains;
    private boolean equals;

    private Versions(int n) {
        this.value = n;
        this.contains = Bukkit.getBukkitVersion().split("-")[0].contains(this.toString());
        this.equals = Bukkit.getBukkitVersion().split("-")[0].equalsIgnoreCase(this.toString());
    }

    private int getValue() {
        return value;
    }

    public boolean Is() {
        return this.contains;
    }

    public boolean IsEquals() {
        return this.equals;
    }

    public String toString() {
        return this.name().replaceAll("_", ".").split("v")[1];
    }

    public boolean esMayorr(Versions v) {
        return (this.getValue() > v.getValue());
    }

    public boolean esMayorIgual(Versions v) {
        return (this.getValue() >= v.getValue());
    }

    public boolean esMenor(Versions v) {
        return (this.getValue() < v.getValue());
    }

    public boolean esMenorIgual(Versions v) {
        return (this.getValue() <= v.getValue());
    }

    public static Versions getVersion() {
        // First pass: exact match against a known version (e.g. "26.1.2").
        for(Versions version : Versions.values()) {
            if(version.IsEquals()) {
                return version;
            }
        }
        // Second pass: unknown patch of a known release line (e.g. a future "26.1.3"
        // hotfix). Fall back to the highest known version sharing the major.minor
        // prefix so newer builds keep working instead of being flagged Unsupported.
        Versions retorno = Versions.vUnsupported;
        for(Versions version : Versions.values()) {
            if(version != Versions.vUnsupported && version.Is()) {
                if(retorno == Versions.vUnsupported || version.getValue() > retorno.getValue()) {
                    retorno = version;
                }
            }
        }
        return retorno;
    }
}
