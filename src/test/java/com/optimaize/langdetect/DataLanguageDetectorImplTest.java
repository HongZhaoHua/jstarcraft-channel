/*
 * Copyright 2011 Fabian Kessler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.optimaize.langdetect;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;

/**
 * Uses all built-in language profiles and tests some simple clean phrases as well as longer texts against them with expected outcome.
 *
 * @author Fabian Kessler
 */
public class DataLanguageDetectorImplTest {

    private final LanguageDetector shortDetector;
    private final LanguageDetector longDetector;

    public DataLanguageDetectorImplTest() throws IOException {
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

        shortDetector = LanguageDetectorBuilder.create(NgramExtractors.standard()).shortTextAlgorithm(100).withProfiles(languageProfiles).build();

        longDetector = LanguageDetectorBuilder.create(NgramExtractors.standard()).shortTextAlgorithm(0).withProfiles(new LanguageProfileReader().readAllBuiltIn()).build();
    }

    @ParameterizedTest
    @MethodSource("shortCleanTexts")
    public void shortTextAlgo(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(shortDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);
        // the detect() method doesn't have enough confidence for all these short texts.
    }

    @ParameterizedTest
    @MethodSource("shortCleanTexts")
    public void longTextAlgoWorkingOnShortText(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(longDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);
        // the detect() method doesn't have enough confidence for all these short texts.
    }

    @ParameterizedTest
    @MethodSource("longerWikipediaTexts")
    public void longTextAlgoWorkingOnLongText(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(longDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);
        assertEquals(longDetector.detect(text).get().getLanguage(), expectedLanguage);
    }

    protected static Object[][] shortCleanTexts() {
        return new Object[][] {

                { "en", shortCleanText("This is some English text.") },

                { "fr", shortCleanText("Ceci est un texte fran??ais.") },

                { "nl", shortCleanText("Dit is een Nederlandse tekst.") },

                { "de", shortCleanText("Dies ist eine deutsche Text") },

                { "km", shortCleanText("??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" + "?????????????????????????????????????????????????????????????????????????????????????????????????????? ?????????????????????????????? ????????????????????????????????????????????? ???????????????????????????????????????????????????") },

                { "bg", shortCleanText("???????????? ???? ???????????? ???? ???????????????? ?????? ?????????????????????? ?????????????? ?? ?????????? ?? ????????????????????????") },

                { "wa", shortCleanText("??ouchal c' est on tecse p??r e walon.") }, };
    }

    private static CharSequence shortCleanText(CharSequence text) {
        return CommonTextObjectFactories.forDetectingShortCleanText().forText(text);
    }

    protected static Object[][] longerWikipediaTexts() {
        return new Object[][] { { "de", largeText(readText("/texts/de-wikipedia-Deutschland.txt")) },

                { "fr", largeText(readText("/texts/fr-wikipedia-France.txt")) },

                { "it", largeText(readText("/texts/it-wikipedia-Italia.txt")) }, };
    }

    private static CharSequence readText(String path) {
        try (InputStream inputStream = DataLanguageDetectorImplTest.class.getResourceAsStream(path)) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String str;
                while ((str = in.readLine()) != null) {
                    sb.append(str);
                }
                return sb.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CharSequence largeText(CharSequence text) {
        return CommonTextObjectFactories.forDetectingOnLargeText().forText(text);
    }

}
