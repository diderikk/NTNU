<template>
  <div id="welcome">
    <div id="welcome-bg-container"></div>
    <div id="welcome-container">
      <h1>Velkommen til gidd, {{ user.forename }}</h1>

      <div id="welcome-text">
        <p>
          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad quae
          provident ex tenetur distinctio a omnis voluptatum? Quidem error
          reiciendis consequuntur.
        </p>
      </div>
      <button id="welcome-button" @click="activityFeed">Kom i gang!</button>
    </div>
  </div>
</template>

<script lang="ts">
import { useRouter } from "vue-router";
import { defineComponent, ref, Ref } from "vue";
import User from "../interfaces/User/User.interface";
import { useStore } from "@/store";

export default defineComponent({
  setup() {
    const router = useRouter();

    const activityFeed = (): void => {
      router.push("/activity-feed");
    };

    const store = useStore();

    const user: Ref<User> = ref(store.getters.user);

    return {
      activityFeed,
      user,
    };
  },
});
</script>

<style scoped lang="scss">
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css");

$primary-color: #282828;
$secondary-color: #ea4b4b;

#welcome-bg-container {
  background-image: url("../../img/welcome-background.svg");
  height: 100%;
  width: 100%;
  top: 0px;
  position: absolute;
  z-index: -1;
  background-position: 0px -100px;
  background-repeat: no-repeat;
  @media only screen and (min-width: 600px) {
    background-position: 400px -150px;
    background-size: 70%;
  }
}

#welcome-container {
  padding: 35px;
  padding-top: 10%;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
    padding-top: 5%;
  }
}

h1 {
  text-align: left;
  width: 70%;
  font-weight: 700;
}

p {
  width: 80%;
  float: right;
  text-align: left;
}

#welcome-button {
  margin-top: 40px;
  @media only screen and (min-width: 600px) {
    margin-left: 50%;
    margin-right: 50%;
    float: unset;
    width: 100px;
  }
}
</style>
