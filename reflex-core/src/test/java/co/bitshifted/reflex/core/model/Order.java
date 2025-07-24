/*
 * Copyright © 2024-2025, Bitshift D.O.O <https://bitshifted.co>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package co.bitshifted.reflex.core.model;

import co.bitshifted.reflex.core.serialize.form.FormDataConverter;
import java.util.HashMap;
import java.util.Map;

public class Order implements FormDataConverter {

  private int id;
  private String code;
  private Address address;

  @Override
  public Map<String, String> toFormData() {
    var map = new HashMap<String, String>();
    map.put("id", Integer.toString(id));
    map.put("Code", code);
    if (address != null) {
      map.put("address-street", address.getStreetAddress());
      map.put("address-city", address.getCity());
    }
    return map;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
