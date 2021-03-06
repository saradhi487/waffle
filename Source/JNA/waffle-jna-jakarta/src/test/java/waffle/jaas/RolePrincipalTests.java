/**
 * Waffle (https://github.com/Waffle/waffle)
 *
 * Copyright (c) 2010-2020 Application Security, Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors: Application Security, Inc.
 */
package waffle.jaas;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class RolePrincipalTests.
 *
 * @author dblock[at]dblock[dot]org
 */
public class RolePrincipalTests {

    /** The role principal. */
    private RolePrincipal rolePrincipal;

    /**
     * Equals_other object.
     */
    @Test
    public void equals_otherObject() {
        Assertions.assertNotEquals(this.rolePrincipal, "");
    }

    /**
     * Equals_same object.
     */
    @Test
    public void equals_sameObject() {
        Assertions.assertEquals(this.rolePrincipal, this.rolePrincipal);
    }

    /**
     * Sets the up.
     */
    @BeforeEach
    public void setUp() {
        this.rolePrincipal = new RolePrincipal("localhost\\Administrator");
    }

    /**
     * Test equals_ symmetric.
     */
    @Test
    public void testEquals_Symmetric() {
        final RolePrincipal x = new RolePrincipal("localhost\\Administrator");
        final RolePrincipal y = new RolePrincipal("localhost\\Administrator");
        Assertions.assertEquals(x, y);
        Assertions.assertEquals(x.hashCode(), y.hashCode());
    }

    /**
     * Test is serializable.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    @Test
    public void testIsSerializable() throws IOException, ClassNotFoundException {
        // serialize
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(this.rolePrincipal);
        }
        assertThat(out.toByteArray().length).isGreaterThan(0);
        // deserialize
        final InputStream in = new ByteArrayInputStream(out.toByteArray());
        final ObjectInputStream ois = new ObjectInputStream(in);
        final RolePrincipal copy = (RolePrincipal) ois.readObject();
        // test
        Assertions.assertEquals(this.rolePrincipal, copy);
        Assertions.assertEquals(this.rolePrincipal.getName(), copy.getName());
    }

}
