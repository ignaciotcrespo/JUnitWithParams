package com.github.ignaciotcrespo.junitwithparams;

import com.github.ignaciotcrespo.junitwithparams.WithParamsRule.ParameterizedStatement;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ParameterizedStatementTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private ParameterizedStatement statement;

    private HashSet<String> usedParams;

    @Before
    public void setUp() throws Exception {
        usedParams = new HashSet<String>();
        statement = new ParameterizedStatement(
                Collections.<String>emptySet(),
                new HashMap<String, String>(),
                null, null, null, usedParams);
    }

    @Test
    public void checkParams_notEmpty() throws Exception {
        // 1) Arrange
        statement = spy(statement);

        usedParams.add("stubParameter");

        doNothing().when(statement).addError(any(Throwable.class));

        // 2) Act
        statement.checkUsedParams();

        // 3) Assert
        ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);
        verify(statement).addError(captor.capture());

        assertThat(captor.getValue()).isInstanceOf(WithParamsException.class);
        assertThat(captor.getValue().getMessage()).contains("stubParameter");
    }
}
