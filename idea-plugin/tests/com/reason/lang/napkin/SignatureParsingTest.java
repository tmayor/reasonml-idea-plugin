package com.reason.lang.napkin;

import java.util.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.reason.lang.core.ORUtil;
import com.reason.lang.core.psi.PsiExternal;
import com.reason.lang.core.psi.PsiFunction;
import com.reason.lang.core.psi.PsiLet;
import com.reason.lang.core.psi.PsiParameter;
import com.reason.lang.core.psi.PsiRecord;
import com.reason.lang.core.psi.PsiRecordField;
import com.reason.lang.core.psi.PsiSignature;
import com.reason.lang.core.psi.PsiSignatureItem;
import com.reason.lang.core.psi.PsiType;
import com.reason.lang.core.signature.ORSignature;
import com.reason.lang.ocaml.OclLanguage;

@SuppressWarnings("ConstantConditions")
public class SignatureParsingTest extends NsParsingTestCase {
    public void test_mandatoryVal() {
        PsiLet let = first(letExpressions(parseCode("let x:int = 1")));

        ORSignature signature = let.getORSignature();
        assertEquals("int", signature.asString(myLanguage));
        assertTrue(signature.isMandatory(0));
    }

    public void test_trimming() {
        PsiLet let = first(letExpressions(parseCode(
                "let statelessComponent:\n  string =>\n  componentSpec(\n    stateless,\n    stateless,\n    noRetainedProps,\n    noRetainedProps,\n    actionless,\n  )\n")));

        PsiSignature signature = let.getPsiSignature();
        assertEquals("string => componentSpec(stateless, stateless, noRetainedProps, noRetainedProps, actionless)", signature.asString(myLanguage));
    }

    public void test_parsingNapkin() {
        PsiLet let = first(letExpressions(parseCode("let padding: (~v:length, ~h:length) => rule")));

        ORSignature signature = let.getORSignature();
        assertEquals(3, signature.getTypes().length);
        assertEquals("(~v:length, ~h:length) => rule", signature.asString(myLanguage));
        assertTrue(signature.isMandatory(0));
        assertTrue(signature.isMandatory(1));
    }

    public void test_optionalFun() {
        PsiLet let = first(letExpressions(parseCode("let x:int => option<string> => string = (a,b) => c")));

        ORSignature signature = let.getORSignature();
        assertTrue(signature.isMandatory(0));
        assertFalse(signature.isMandatory(1));
    }

    public void test_optionalFunParameters() {
        PsiLet let = first(letExpressions(parseCode("let x = (a:int, b:option<string>, c:bool=false, d:float=?) => 3")));

        PsiFunction function = (PsiFunction) let.getBinding().getFirstChild();
        List<PsiParameter> parameters = new ArrayList<>(function.getParameters());

        assertTrue(parameters.get(0).getPsiSignature().asHMSignature().isMandatory(0));
        assertFalse(parameters.get(1).getPsiSignature().asHMSignature().isMandatory(0));
        //        assertFalse(parameters.get(2).getPsiSignature().asHMSignature().isMandatory(0));
        assertEquals("bool", parameters.get(2).getPsiSignature().asString(myLanguage));
        //        assertFalse(parameters.get(3).getPsiSignature().asHMSignature().isMandatory(0));
        assertEquals("float", parameters.get(3).getPsiSignature().asString(myLanguage));
    }

    public void test_unitFunParameter() {
        PsiLet e = first(letExpressions(parseCode("let x = (~color=\"red\", ~radius=1, ()) => 1")));

        PsiFunction function = (PsiFunction) e.getBinding().getFirstChild();
        List<PsiParameter> parameters = new ArrayList<>(function.getParameters());

        assertSize(3, parameters);
    }

    public void test_jsObject() {
        PsiType psiType = first(typeExpressions(parseCode("type props = { @bs.optional dangerouslySetInnerHTML: {. \"__html\": string} }")));

        PsiRecord record = (PsiRecord) psiType.getBinding().getFirstChild();
        List<PsiRecordField> fields = new ArrayList<>(record.getFields());

        assertEquals(1, fields.size());
        assertEquals("{. \"__html\": string}", fields.get(0).getPsiSignature().getText());
    }

    public void test_externalFun() {
        PsiExternal e = first(externalExpressions(parseCode("external refToJsObj : reactRef => {..} = \"%identity\"")));

        ORSignature signature = e.getORSignature();
        assertSize(2, ORUtil.findImmediateChildrenOfClass(e.getPsiSignature(), PsiSignatureItem.class));
        assertEquals("reactRef => {..}", signature.asString(myLanguage));
    }

    public void test_externalFun2() {
        PsiExternal e = first(externalExpressions(parseCode("external requestAnimationFrame: (unit => unit) => animationFrameID = \"\"")));

        ORSignature signature = e.getORSignature();
        Collection<PsiSignatureItem> signatureItems = PsiTreeUtil.findChildrenOfType(e.getPsiSignature(), PsiSignatureItem.class);
        assertSize(3, signatureItems);
        Iterator<PsiSignatureItem> itSig = signatureItems.iterator();
        assertEquals("unit", itSig.next().getText());
        assertEquals("unit", itSig.next().getText());
        assertEquals("animationFrameID", itSig.next().getText());
    }

    public void test_option() {
        PsiExternal e = first(externalExpressions(parseCode("external e : option<show> = \"\"")));

        PsiSignatureItem sigItem = ORUtil.findImmediateChildrenOfClass(e.getPsiSignature(), PsiSignatureItem.class).iterator().next();
        assertEquals("option<show>", sigItem.asText(myLanguage));
    }

    //public void test_defaultOptional() {
    //    PsiLet let = first(letExpressions(parseCode("let createAction: (string, payload, ~meta: 'meta=?, unit) => opaqueFsa")));
    //    ORSignature signature = let.getORSignature();
    //    assertEquals("(string, payload, ~meta: 'meta=?, unit) => opaqueFsa", signature.asString(myLanguage));
    //}
}
