/*
 * Copyright (C) 2017 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.keraiai.soap.security;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the SecurityUtils class.
 *
 * @author Scot P. Floess
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityUtilsTest {

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Constructor constructor = SecurityUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance(new Object[0]);
    }

    /**
     * Test computing a URL string with a null protocol.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_protocol_null_host() {
        SecurityUtils.computeUrlString(null, "foo.com");
    }

    /**
     * Test computing a URL string with an empty protocol.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_protocol_empty_host() {
        SecurityUtils.computeUrlString("", "foo.com");
    }

    /**
     * Test computing a URL string with a blank protocol.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_protocol_blank_host() {
        SecurityUtils.computeUrlString("  ", "foo.com");
    }

    /**
     * Test computing a URL string with a null host.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_protocol_host_null() {
        SecurityUtils.computeUrlString("http", null);
    }

    /**
     * Test computing a URL string with an empty host.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_protocol_host_empty() {
        SecurityUtils.computeUrlString("https", "");
    }

    /**
     * Test computing a URL string with a blank host.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_protocol_host_blank() {
        SecurityUtils.computeUrlString("ftp", "  ");
    }

    /**
     * Test computing a good URL string.
     */
    @Test
    public void test_computeUrlString_protocol_host() {
        Assert.assertEquals("Should have gotten the correct compute URL", "https://foo.com", SecurityUtils.computeUrlString("https", "foo.com"));
    }

    /**
     * Test a null server URL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_URL_null() {
        SecurityUtils.computeUrlString((URL) null);
    }

    /**
     * Test a good server URL.
     */
    @Test
    public void test_computeUrlString_URL() throws MalformedURLException {
        final URL url = new URL("https://foo.com/alpha/beta.txt");
        Assert.assertEquals("Should have compute the correct URL string", "https://foo.com", SecurityUtils.computeUrlString(url));
    }

    /**
     * Test a null URL string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_String_null() {
        SecurityUtils.computeUrlString((String) null);
    }

    /**
     * Test an empty URL string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_String_empty() {
        SecurityUtils.computeUrlString("");
    }

    /**
     * Test a blank URL string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_String_blank() {
        SecurityUtils.computeUrlString("  ");
    }

    /**
     * Test a malformed URL string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_computeUrlString_String_malformed() {
        SecurityUtils.computeUrlString("htp:/foo&alpha");
    }

    /**
     * Test a good URL string.
     */
    @Test
    public void test_computeUrlString_String() {
        Assert.assertEquals("Should be the correct URL string", "https://alpha.com", SecurityUtils.computeUrlString("https://alpha.com/theta/a/b/c/foo.txt"));
    }
}
