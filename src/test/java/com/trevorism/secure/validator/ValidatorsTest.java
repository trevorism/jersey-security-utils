package com.trevorism.secure.validator;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorsTest {

    @Test
    public void removeValidator() {
        Validators.removeValidator(Validators.localhostTokenValidator);
        assertEquals(2, Validators.allValidators.size());
    }
}