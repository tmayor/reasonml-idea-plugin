package com.reason.lang.napkin;

import java.util.*;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.reason.ide.files.FileBase;
import com.reason.lang.core.ORUtil;
import com.reason.lang.core.psi.PsiBinaryCondition;
import com.reason.lang.core.psi.PsiFunction;
import com.reason.lang.core.psi.PsiLet;
import com.reason.lang.core.psi.PsiPatternMatch;
import com.reason.lang.core.psi.PsiPatternMatchBody;
import com.reason.lang.core.psi.PsiScopedExpr;
import com.reason.lang.core.psi.PsiSwitch;
import com.reason.lang.core.psi.PsiUpperSymbol;
import com.reason.lang.core.psi.PsiVariantDeclaration;

@SuppressWarnings("ConstantConditions")
public class SwitchParsingTest extends NsParsingTestCase {
    public void test_patternUSymbol() {
        FileBase f = parseCode("switch x { | Variant1(x) => x () | Variant2 => () }");

        assertEquals(1, childrenCount(f));
        PsiSwitch switch_ = first(PsiTreeUtil.findChildrenOfType(f, PsiSwitch.class));
        assertNotNull(switch_);

        PsiBinaryCondition condition = ORUtil.findImmediateFirstChildOfClass(switch_, PsiBinaryCondition.class);
        assertEquals("x", condition.getText());

        PsiScopedExpr scope = ORUtil.findImmediateFirstChildOfClass(switch_, PsiScopedExpr.class);
        List<PsiPatternMatch> patterns = ORUtil.findImmediateChildrenOfClass(scope, PsiPatternMatch.class);
        assertSize(2, patterns);

        assertEmpty(PsiTreeUtil.findChildrenOfType(switch_, PsiVariantDeclaration.class));

        // first pattern
        PsiPatternMatch p1 = patterns.get(0);
        PsiPatternMatchBody p1Body = p1.getBody();
        assertEquals("x ()", p1Body.getText());
        // ?? assertNotNull(ORUtil.findImmediateFirstChildOfClass(p1Body, PsiUnit.class));

        // second pattern
        PsiPatternMatch p2 = patterns.get(1);
        PsiPatternMatchBody p2Body = p2.getBody();
        assertEquals("()", p2Body.getText());
        //assertNotNull(ORUtil.findImmediateFirstChildOfClass(p2Body, PsiUnit.class));
    }

    public void test_patternOption() {
        FileBase f = parseCode("switch x { | Some(x) => x () | None => () }");

        assertEquals(1, childrenCount(f));
        PsiSwitch switch_ = first(PsiTreeUtil.findChildrenOfType(f, PsiSwitch.class));
        assertNotNull(switch_);

        PsiBinaryCondition condition = ORUtil.findImmediateFirstChildOfClass(switch_, PsiBinaryCondition.class);
        assertEquals("x", condition.getText());

        PsiScopedExpr scope = ORUtil.findImmediateFirstChildOfClass(switch_, PsiScopedExpr.class);
        List<PsiPatternMatch> patterns = ORUtil.findImmediateChildrenOfClass(scope, PsiPatternMatch.class);
        assertSize(2, patterns);

        assertEmpty(PsiTreeUtil.findChildrenOfType(switch_, PsiVariantDeclaration.class));

        // first pattern
        PsiPatternMatch p1 = patterns.get(0);
        PsiPatternMatchBody p1Body = p1.getBody();
        assertEquals("x ()", p1Body.getText());
        //assertNotNull(ORUtil.findImmediateFirstChildOfClass(p1Body, PsiUnit.class));

        // second pattern
        PsiPatternMatch p2 = patterns.get(1);
        PsiPatternMatchBody p2Body = p2.getBody();
        assertEquals("()", p2Body.getText());
        //assertNotNull(ORUtil.findImmediateFirstChildOfClass(p2Body, PsiUnit.class));
    }

    public void test_patternTokenType() {
        PsiFile psiFile = parseCode("switch action { | Incr => counter + 1 }");

        PsiSwitch switch_ = first(PsiTreeUtil.findChildrenOfType(psiFile, PsiSwitch.class));
        Collection<PsiPatternMatch> patterns = PsiTreeUtil.findChildrenOfType(switch_, PsiPatternMatch.class);
        assertSize(1, patterns);
        PsiPatternMatch psiPatternMatch = patterns.iterator().next();
        PsiPatternMatch patternMatch = patterns.iterator().next();
        PsiUpperSymbol variant = ORUtil.findImmediateFirstChildOfClass(patternMatch, PsiUpperSymbol.class);
        assertTrue(variant.isVariant());
        assertEquals("Incr", variant.getText());
        assertEquals("counter + 1", psiPatternMatch.getBody().getText());
    }

    public void test_patternMatch() {
        FileBase f = parseCode("switch p { | Typedtree.Partial => \"Partial\" | Total => \"Total\" }");
        PsiSwitch e = first(PsiTreeUtil.findChildrenOfType(f, PsiSwitch.class));

        List<PsiPatternMatch> patterns = new ArrayList<>(
                ORUtil.findImmediateChildrenOfClass(ORUtil.findImmediateFirstChildOfClass(e, PsiScopedExpr.class), PsiPatternMatch.class));
        assertSize(2, patterns);

        PsiPatternMatch m1 = patterns.get(0);
        assertEquals("Typedtree", PsiTreeUtil.findChildOfType(m1, PsiUpperSymbol.class).getText());
        assertEquals("\"Partial\"", m1.getBody().getText());

        PsiPatternMatch m2 = patterns.get(1);
        assertEquals("\"Total\"", m2.getBody().getText());
    }

    public void test_patternMatch2() {
        FileBase f = parseCode("let greeting = name => switch name { | FirstName(fn) => \"hello \" ++ fn | LastName(ln) => \"hello \" ++ ln }");
        PsiSwitch e = first(PsiTreeUtil.findChildrenOfType(f, PsiSwitch.class));

        List<PsiPatternMatch> patterns = new ArrayList<>(
                ORUtil.findImmediateChildrenOfClass(ORUtil.findImmediateFirstChildOfClass(e, PsiScopedExpr.class), PsiPatternMatch.class));
        assertSize(2, patterns);
    }

    public void test_let() {
        PsiLet e = first(letExpressions(parseCode("let makeId = () => switch id { | None => text | Some(i) => i }")));

        PsiFunction function = (PsiFunction) e.getBinding().getFirstChild();
        assertEquals("switch id { | None => text | Some(i) => i }", function.getBody().getText());
    }

    public void test_react() {
        FileBase psiFile = parseCode(
                "switch reasonStateUpdate { | NoUpdate => (None, curTotalState) | Update(nextReasonState) => ( None, {\"reasonState\": nextReasonState}, ) }");
        assertEquals(1, childrenCount(psiFile));
        PsiSwitch e = (PsiSwitch) psiFile.getChildren()[0];

        assertEquals("reasonStateUpdate", e.getCondition().getText());
        assertSize(2, e.getPatterns());
    }
}
