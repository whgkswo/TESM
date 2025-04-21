package net.whgkswo.tesm.lang;

import java.util.Random;

public class LanguageHelper {
    /**
     * 한글 단어에 적절한 조사를 반환합니다.
     * @param word 대상 단어
     * @return 받침이 있으면 "을", 없으면 "를" 반환
     */
    public static String getMarker(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        char lastChar = word.charAt(word.length() - 1);

        // 한글 유니코드 범위 확인 (AC00-D7A3)
        if (lastChar < 0xAC00 || lastChar > 0xD7A3) {
            // 한글이 아닌 경우 기본값 반환
            return "을";
        }

        // 받침 유무 확인
        // 한글 유니코드 계산법: (초성 * 21 + 중성) * 28 + 종성 + 0xAC00
        // 종성이 없는 경우 종성 값은
        return (lastChar - 0xAC00) % 28 > 0 ? "을" : "를";
    }

    /**
     * 한글 단어에 적절한 주격 조사를 반환합니다.
     * @param word 대상 단어
     * @return 받침이 있으면 "이", 없으면 "가" 반환
     */
    public static String getSubjectMarker(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        char lastChar = word.charAt(word.length() - 1);

        if (lastChar < 0xAC00 || lastChar > 0xD7A3) {
            return "이";
        }

        return (lastChar - 0xAC00) % 28 > 0 ? "이" : "가";
    }

    /**
     * 한글 단어에 적절한 존재 조사를 반환합니다.
     * @param word 대상 단어
     * @return 받침이 있으면 "은", 없으면 "는" 반환
     */
    public static String getTopicMarker(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        char lastChar = word.charAt(word.length() - 1);

        if (lastChar < 0xAC00 || lastChar > 0xD7A3) {
            return "은";
        }

        return (lastChar - 0xAC00) % 28 > 0 ? "은" : "는";
    }

    /**
     * 한글 단어에 적절한 호격 조사를 반환합니다.
     * @param word 대상 단어
     * @return 받침이 있으면 "아", 없으면 "야" 반환
     */
    public static String getVocativeMarker(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        char lastChar = word.charAt(word.length() - 1);

        if (lastChar < 0xAC00 || lastChar > 0xD7A3) {
            return "아";
        }

        return (lastChar - 0xAC00) % 28 > 0 ? "아" : "야";
    }

    /**
     * 한글 단어에 적절한 조사를 붙여서 반환합니다.
     * @param word 대상 단어
     * @param type 조사 타입 ("을/를", "은/는", "이/가", "아/야")
     * @return 단어에 조사가 붙은 문자열
     */
    public static String appendMarker(String word, String type) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        String marker = "";
        switch (type) {
            case "을/를":
                marker = getMarker(word);
                break;
            case "은/는":
                marker = getTopicMarker(word);
                break;
            case "이/가":
                marker = getSubjectMarker(word);
                break;
            case "아/야":
                marker = getVocativeMarker(word);
                break;
            default:
                return word;
        }

        return word + marker;
    }

    public static String toSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2") // 카멜케이스 → 스네이크케이스 변환
                .replace(' ', '_') // 공백 → 언더스코어
                .toLowerCase();
    }

    public static String generateRandomCode(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }
}
