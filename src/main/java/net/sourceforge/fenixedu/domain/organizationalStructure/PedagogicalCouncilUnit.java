/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

public class PedagogicalCouncilUnit extends PedagogicalCouncilUnit_Base {

    protected PedagogicalCouncilUnit() {
        super();
        super.setType(PartyTypeEnum.PEDAGOGICAL_COUNCIL);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    @Override
    protected List<Group> getDefaultGroups() {
        List<Group> groups = super.getDefaultGroups();

        groups.add(RoleType.PEDAGOGICAL_COUNCIL.actualGroup());

        return groups;
    }

    @Override
    public Collection<Person> getPossibleGroupMembers() {
        return RoleType.PEDAGOGICAL_COUNCIL.actualGroup().getMembers().stream().map(User::getPerson).collect(Collectors.toSet());
    }

    public static PedagogicalCouncilUnit getPedagogicalCouncilUnit() {
        final Set<Party> parties = PartyType.getPartiesSet(PartyTypeEnum.PEDAGOGICAL_COUNCIL);
        return parties.isEmpty() ? null : (PedagogicalCouncilUnit) parties.iterator().next();
    }

}
