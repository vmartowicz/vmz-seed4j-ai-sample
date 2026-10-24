package fr.vmz.seed4j.sample.ai.shared.collection.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import fr.vmz.seed4j.sample.ai.UnitTest;

@UnitTest
class VmzSeed4JAiSampleApplicationCollectionsTest {

  @Nested
  @DisplayName("Collections")
  class VmzSeed4JAiSampleApplicationCollectionsCollectionsTest {

    @Test
    void shouldGetEmptyImmutableCollectionFromNullCollection() {
      Collection<Object> input = null;
      Collection<Object> collection = VmzSeed4JAiSampleApplicationCollections.immutable(input);

      assertThat(collection).isEmpty();
      assertThatThrownBy(collection::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldGetImmutableCollectionFromMutableCollection() {
      Collection<String> input = new ArrayList<>();
      input.add("value");
      Collection<String> collection = VmzSeed4JAiSampleApplicationCollections.immutable(input);

      assertThat(collection).containsExactly("value");
      assertThatThrownBy(collection::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
  }

  @Nested
  @DisplayName("Set")
  class VmzSeed4JAiSampleApplicationCollectionsSetTest {

    @Test
    void shouldGetEmptyImmutableCollectionFromNullCollection() {
      Set<Object> input = null;
      Set<Object> set = VmzSeed4JAiSampleApplicationCollections.immutable(input);

      assertThat(set).isEmpty();
      assertThatThrownBy(set::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldGetImmutableCollectionFromMutableCollection() {
      Set<String> input = new HashSet<>();
      input.add("value");
      Set<String> set = VmzSeed4JAiSampleApplicationCollections.immutable(input);

      assertThat(set).containsExactly("value");
      assertThatThrownBy(set::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
  }

  @Nested
  @DisplayName("List")
  class VmzSeed4JAiSampleApplicationCollectionsListTest {

    @Test
    void shouldGetEmptyImmutableCollectionFromNullCollection() {
      List<Object> input = null;
      List<Object> list = VmzSeed4JAiSampleApplicationCollections.immutable(input);

      assertThat(list).isEmpty();
      assertThatThrownBy(list::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldGetImmutableCollectionFromMutableCollection() {
      List<String> input = new ArrayList<>();
      input.add("value");
      List<String> list = VmzSeed4JAiSampleApplicationCollections.immutable(input);

      assertThat(list).containsExactly("value");
      assertThatThrownBy(list::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
  }

  @Nested
  @DisplayName("Map")
  class VmzSeed4JAiSampleApplicationMapTest {

    @Test
    void shouldGetEmptyImmutableMapFromNullMap() {
      Map<Object, Object> input = null;
      Map<Object, Object> map = VmzSeed4JAiSampleApplicationCollections.immutable(input);

      assertThat(map).isEmpty();
      assertThatThrownBy(map::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldGetImmutableMapFromMutableMap() {
      Map<String, String> input = new HashMap<>();
      input.put("key", "value");
      Map<String, String> map = VmzSeed4JAiSampleApplicationCollections.immutable(input);

      assertThat(map).containsExactly(Map.entry("key", "value"));
      assertThatThrownBy(map::clear).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
  }
}
