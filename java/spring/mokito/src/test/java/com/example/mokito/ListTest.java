package com.example.mokito;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListTest {
    @Test
    public void testSize_multipleReturn() {
        List listMock = mock(List.class);

        when(listMock.size()).thenReturn(10).thenReturn(20);

        assertEquals(10, listMock.size());
        assertEquals(20, listMock.size());
    }

    @Test
    public void testGet_Specific() {
        List listMock = mock(List.class);
        when(listMock.get(0)).thenReturn("Ala ma kota");
        when(listMock.get(0)).thenReturn("Ala ma kota");

        assertEquals("Ala ma kota", listMock.get(0));
        assertEquals(null, listMock.get(1));
    }

    @Test
    public void testGet_generic() {
        List listMock = mock(List.class);
        when(listMock.get(Mockito.anyInt())).thenReturn("Ala nie ma kota");
        when(listMock.get(0)).thenReturn("Ala ma kota");
        when(listMock.get(1)).thenReturn("Ala ma 1 kota");

        assertEquals("Ala ma kota", listMock.get(0));
        assertEquals("Ala ma 1 kota", listMock.get(1));
        assertEquals("Ala nie ma kota", listMock.get(2));
    }

}
