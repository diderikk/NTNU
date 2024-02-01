<template>
  <div class="the-header">
    <span id="login-check" class="logo-nav" v-if="loggedIn">
      <router-link to="/activity-feed">
        <img src="../../img/logo.png" alt="Logo" height="20" />
      </router-link>
    </span>
    <span v-else class="logo-nav">
      <router-link to="/">
        <img src="../../img/logo.png" alt="Logo" height="20" />
      </router-link>
    </span>
    <div class="menu-box">
      <span v-if="loggedIn">
        <a href="#" class="icon" @click="toggleMenu">
          <i class="fa fa-bars"></i>
        </a>
      </span>
      <span v-else>
        <a href="#" class="icon" @click="toggleMenu">
          <i class="fa fa-bars"></i>
        </a>
      </span>
    </div>
    <div id="menu-toggle" v-if="menuVisible">
      <span id="close-menu" class="icon" @click="toggleMenu">
        <i class="fa fa-times"></i>
      </span>
      <div class="seperator"></div>
      <span v-if="loggedIn">
        <router-link
          id="logged-in-option"
          v-for="(option, index) in loggedInOptions"
          :key="index"
          class="menu-options"
          :to="option.path"
          >{{ option.title }}</router-link
        >
        <span class="menu-options find-activity" @click="findActivity"
          >Finn aktivitet</span
        >
        <span class="menu-options logout" @click="logout"
          ><i class="fa fa-power-off" aria-hidden="true"></i> Logg ut</span
        >
      </span>

      <span v-else>
        <router-link
          v-for="(option, index) in loggedOutOptions"
          :key="index"
          class="menu-options"
          :to="option.path"
        >
          {{ option.title }}
        </router-link>
      </span>
    </div>
  </div>
</template>

<script lang="ts">
import { useStore } from "@/store";
import { computed, defineComponent, Ref, ref } from "vue";
import { useRouter } from "vue-router";
import MenuOption from "../interfaces/MenuOption.interface";

/**
 * Defines components to be used
 *
 * @param loggedInOptions:     Side menu options when a user is logged in  - Array
 * @param loggedOutOptions:    Side menu options when a user is logged out - Array
 * @param loggedIn:            Check if user is logged in or out           - Boolean
 * @param menuVisible:         Check if menu is visible or not             - Boolean
 * @param toggleMenu:          Method to toggle the visibility of the menu
 */
export default defineComponent({
  name: "TheHeader",
  setup() {
    const store = useStore();
    const router = useRouter();
    //Adding afterEach to make sure the menu closes afte every route
    router.afterEach((to, from) => {
      //Have to check that the paths are not equal, since closing a window when staying on the same page gives bad user experience, and also leads to the menu not working
      if (to.fullPath !== from.fullPath) {
        menuVisible.value = false;
      }
    });

    const loggedInOptions: Ref<MenuOption[]> = computed(() => {
      return [
        { title: "Min profil", path: `/profile/${store.getters.user.userId}` },
        { title: "Mine aktiviteter", path: "/calendar" },
      ];
    });
    const loggedOutOptions: Ref<MenuOption[]> = ref([
      { title: "Logg inn", path: "/log-in" },
      { title: "Opprett bruker", path: "/sign-up" },
    ]);
    //Have to use optional ?, to make the unit tests run.
    const loggedIn = computed(() => store?.getters?.isLoggedIn);
    const menuVisible = ref(false);

    const toggleMenu = (): void => {
      menuVisible.value = !menuVisible.value;
    };

    const logout = (): void => {
      store.dispatch("logout");
      router.replace("/log-in");
    };

    const findActivity = (): void => {
      router.push("/activity-feed");
    };

    return {
      loggedInOptions,
      loggedOutOptions,
      loggedIn,
      menuVisible,
      toggleMenu,
      logout,
      findActivity,
    };
  },
});
</script>

<style lang="scss">
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css");

$primary-color: #282828;
$secondary-color: #ea4b4b;

.the-header {
  background-color: #ffffff;
  z-index: 2;
  position: fixed;
  top: 0px;
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  box-shadow: 0px 13px 26px 0px RGBA(0 0 0 / 4%);
  @media only screen and (min-width: 600px) {
    margin-bottom: 30px;
  }
}

.menu-box {
  padding: 25px;
  text-align: right;
  grid-column: 3;
}

a.icon {
  padding-top: 100px;
  color: $primary-color;
}

.fa-bars {
  font-size: 1.2em;
}

#menu-toggle {
  padding-top: 25px;
  position: fixed;
  background-color: #f7f7f7;
  height: 100%;
  width: 200px;
  right: 0px;
  top: 0px;
  z-index: 2;
}

.logo-nav {
  padding: 25px;
  justify-self: start;
}

.menu-options {
  padding: 20px;
  margin: 10px;
  text-align: left;
  border-radius: 5px;
  text-decoration: none;
  display: block;
  cursor: pointer;
}

.menu-options:hover {
  background-color: $secondary-color;
  color: #ffffff !important;
}

.menu-options:visited {
  color: $primary-color;
}

.menu-options.find-activity {
  border-radius: 0;
  border: 3px $secondary-color solid;
  color: #ea4b4b;
}

.menu-options.logout {
  position: absolute;
  bottom: 20px;
}

.menu-options.logout:hover {
  color: $secondary-color !important;
  background-color: unset;
}

#close-menu {
  padding-top: 300px;
  margin-left: 135px;
  cursor: pointer;
  padding-bottom: 40px;
}

.menu-options {
  margin-top: 30px;
}

.fa-times {
  font-size: 1.2em;
}
</style>
