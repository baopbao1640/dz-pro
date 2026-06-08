package com.platform.core.framework.security.authz;

import static org.assertj.core.api.Assertions.assertThat;

import com.platform.core.framework.security.annotation.RequiresPermission;
import com.platform.core.framework.security.context.CurrentUser;
import com.platform.core.framework.security.route.RouteDefinition;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthorizationFoundationTest {

  @RequiresPermission({"system:user:list", "system:role:list"})
  void protectedAction() {}

  @Test
  void dataScopeKeepsDatabaseCodesStable() {
    assertThat(DataScope.ALL.getCode()).isEqualTo("1");
    assertThat(DataScope.CUSTOM.getCode()).isEqualTo("2");
    assertThat(DataScope.DEPT.getCode()).isEqualTo("3");
    assertThat(DataScope.DEPT_AND_CHILD.getCode()).isEqualTo("4");
    assertThat(DataScope.SELF.getCode()).isEqualTo("5");
    assertThat(DataScope.fromCode("4")).contains(DataScope.DEPT_AND_CHILD);
    assertThat(DataScope.fromCode("9")).isEmpty();
  }

  @Test
  void permissionAnnotationIsAvailableForFutureAopChecks() throws NoSuchMethodException {
    Method method = AuthorizationFoundationTest.class.getDeclaredMethod("protectedAction");

    RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);

    assertThat(annotation.value()).containsExactly("system:user:list", "system:role:list");
    assertThat(annotation.matchMode()).isEqualTo(PermissionMatchMode.ALL);
  }

  @Test
  void currentUserCopiesMutableAuthorizationSets() {
    Set<String> permissions = new java.util.HashSet<>(Set.of("system:user:list"));

    CurrentUser currentUser =
        new CurrentUser(1L, "keycloak-sub", "admin", 10L, Set.of("admin"), permissions, false);
    permissions.add("system:role:list");

    assertThat(currentUser.permissionCodes()).containsExactly("system:user:list");
  }

  @Test
  void routeDefinitionDefaultsChildrenToEmptyList() {
    RouteDefinition route =
        new RouteDefinition("System", "/system", "Layout", null, null, List.of());

    assertThat(route.children()).isEmpty();
  }
}
