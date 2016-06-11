/*
 * Copyright (C) 2016 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.keraiai.soap.port;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the UrlUtils utility class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class UrlUtilsTest {

    @Mock
    BindingProvider port;

    Map<String, Object> requestContext;

    @Before
    public void setup() {
        requestContext = new HashMap<>();

        Mockito.when(port.getRequestContext()).thenReturn(requestContext);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Constructor constructor = UrlUtils.class.getDeclaredConstructor(new Class[0]);
        constructor.setAccessible(true);
        constructor.newInstance(new Object[0]);

        System.out.println("C -> " + constructor);
    }

    /**
     * Test computing a URL string using strings.
     */
    @Test
    public void test_computeUrlString() {
        Assert.assertEquals("Should be the same string", "https://foo.com/bar/alpha", UrlUtils.computeUrlString("https", "foo.com", "bar", "alpha"));
    }

    /**
     * Test computing a URL string using a url.
     */
    @Test
    public void test_computeUrlString_url() throws MalformedURLException {
        Assert.assertEquals("Should be the same string", "https://foo.com/bar/alpha", UrlUtils.computeUrlString(new URL("https://foo.com/not/here/now"), "bar", "alpha"));
    }

    /**
     * Test computing a URL string using a null url where the serverUrl has more subpaths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_paths_null() throws MalformedURLException {
        UrlUtils.computeUrlString((String) null, "bar", "alpha");
    }

    /**
     * Test computing a URL string using a blank url where the serverUrl has more subpaths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_paths_blank() throws MalformedURLException {
        UrlUtils.computeUrlString("  ", "bar", "alpha");
    }

    /**
     * Test computing a URL string using an empty url where the serverUrl has more subpaths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_paths_empty() throws MalformedURLException {
        UrlUtils.computeUrlString("", "bar", "alpha");
    }

    /**
     * Test computing a URL string using a maleformed url where the serverUrl has more subpaths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_paths_malformed() throws MalformedURLException {
        UrlUtils.computeUrlString("h ttp:_fo", "bar", "alpha");
    }

    /**
     * Test computing a URL string using a proper url where the serverUrl has more subpaths.
     */
    @Test
    public void test_computeUrlString_paths() throws MalformedURLException {
        Assert.assertEquals("Should be the same string", "http://foo.com/bar/alpha", UrlUtils.computeUrlString("http://foo.com/theta/papapapa", "bar", "alpha"));
    }

    /**
     * Test setting a URL using a null url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setUrl_null() throws MalformedURLException {
        UrlUtils.setUrl(port, null, "bar", "alpha");
    }

    /**
     * Test setting a URL using a blank url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setUrl_blank() throws MalformedURLException {
        UrlUtils.setUrl(port, "  ", "bar", "alpha");
    }

    /**
     * Test setting a URL using an empty url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setUrl_empty() throws MalformedURLException {
        UrlUtils.setUrl(port, "", "bar", "alpha");
    }

    /**
     * Test setting a URL using a maleformed url.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setUrl_malformed() throws MalformedURLException {
        UrlUtils.setUrl(port, "h ttp:_fo", "bar", "alpha");
    }

    /**
     * Test setting a URL using a proper url.
     */
    @Test
    public void test_setUrl() throws MalformedURLException {
        UrlUtils.setUrl(port, "http://foo.com/theta/apapapapa", "bar", "alpha");

        Assert.assertFalse("Should contain data", requestContext.isEmpty());
        Assert.assertEquals("Should only be one element", 1, requestContext.size());

        Assert.assertEquals("Should be correct url", "http://foo.com/bar/alpha", requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }
}
