/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.integration.model;

import co.bitshifted.reflex.core.serialize.form.FormDataConverter;
import java.util.HashMap;
import java.util.Map;

public class Order implements FormDataConverter {

  private int id;
  private Person person;

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public Person getPerson() {
    return person;
  }

  @Override
  public Map<String, String> toFormData() {
    var map = new HashMap<String, String>();
    map.put("id", Integer.toString(id));
    if (person != null) {
      map.put("person_name", person.getName());
      map.put("person_age", Integer.toString(person.getAge()));
      if (person.getAddress() != null) {
        map.put("person_address_city", person.getAddress().getCity());
      }
    }
    return map;
  }
}
