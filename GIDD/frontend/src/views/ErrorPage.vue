<template>
<div>
  <h1>Ops! Noe gikk galt!</h1>
  <button @click="backToFeed" alt="Knapp for å gå tilbake til applikasjonen">
    Gå tilbake og prøv igjen
  </button>
  <button class="error-logout" @click="logout" alt="Knapp for å logge ut">
    <i class="fa fa-power-off" aria-hidden="true"></i>Logg ut
  </button>
</div>
</template>

<script lang="ts">
import { useRouter } from "vue-router";
import { defineComponent } from "vue";
import { store } from "@/store";

export default defineComponent({
  setup() {
    const router = useRouter();

    /**
     * Sends the user back to the activity feed
     */
    const backToFeed = (): void => {
      router.push("/activity-feed");
    };

    /**
     * Logs the user out; useful in case user gets stuck in
     * error loop
     */
    const logout = (): void => {
      store.dispatch("logout");
      router.replace("/log-in");
    };

    return {
      backToFeed,
      logout,
    };
  },
});
</script>
