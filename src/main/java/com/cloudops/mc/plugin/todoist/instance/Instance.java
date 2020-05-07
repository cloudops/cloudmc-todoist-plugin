package com.cloudops.mc.plugin.todoist.instance;

import java.util.Objects;
import java.util.StringJoiner;

import com.cloudops.mc.plugin.sdk.annotations.Entity;
import com.cloudops.mc.plugin.sdk.entity.ServiceEntity;

@Entity(value = "instances")
public class Instance implements ServiceEntity {
   private String id;
   private String name;

   @Override
   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Instance.class.getSimpleName() + "[", "]")
              .add("id='" + id + "'")
              .add("name='" + name + "'")
              .toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o){
         return true;
      }
      if (o == null || getClass() != o.getClass()){
         return false;
      }
      if (!super.equals(o)){
         return false;
      }
      Instance instance = (Instance) o;
      return  Objects.equals(id, instance.id) &&
              Objects.equals(name, instance.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), id, name);
   }
}
