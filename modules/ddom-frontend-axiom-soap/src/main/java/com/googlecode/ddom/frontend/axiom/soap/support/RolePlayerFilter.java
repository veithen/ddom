/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.soap.support;

import java.util.List;

import org.apache.axiom.soap.RolePlayer;

import com.google.code.ddom.collections.Filter;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPHeaderBlock;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;

public class RolePlayerFilter implements Filter<AxiomSOAPHeaderBlock> {
    private final RolePlayer rolePlayer;
    private final String namespaceURI;

    public RolePlayerFilter(RolePlayer rolePlayer, String namespaceURI) {
        this.rolePlayer = rolePlayer;
        this.namespaceURI = namespaceURI;
    }

    public boolean accept(AxiomSOAPHeaderBlock header) {
        try {
            if (namespaceURI != null && !header.coreGetNamespaceURI().equals(namespaceURI)) {
                return false;
            }
            String role = header.getRole();
            SOAPVersionEx version = header.getSOAPVersionEx();
            if (version.isUltimateReceiverRole(role)) {
                return rolePlayer == null || rolePlayer.isUltimateDestination();
            }
            if (role.equals(version.getSOAPVersion().getNextRoleURI())) {
                return true;
            }
            if (version.isNoneRole(role)) {
                return false;
            }
            if (rolePlayer != null) {
                List roles = rolePlayer.getRoles();
                if (roles != null && roles.contains(role)) {
                    return true;
                }
            }
            return false;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
}
