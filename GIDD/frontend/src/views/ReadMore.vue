<template>
  <div id="read-more">
    <div id="read-more-content">
      <button @click="scrollBack" class="back-button">
        <i class="fa fa-arrow-left" aria-hidden="true"></i>
        Gå tilbake
      </button>
      <h1>Om Gidd</h1>
      <div id="textbox1">
        <!--Koper denne om det er behov for flere-->
        <h3>Lorem Impsum</h3>
        <h5>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce in
          finibus ipsum. Nunc tincidunt turpis in felis pharetra ultrices.
          Integer sed molestie mi, eget hendrerit erat. Quisque eget consectetur
          velit, vitae volutpat dui. Donec purus elit, accumsan a ipsum ut,
          viverra ullamcorper massa. Nam ornare dapibus bibendum. Donec viverra,
          lacus vel interdum fringilla, felis nibh cursus felis, non luctus
          sapien libero at quam. Donec dignissim nibh eu ligula scelerisque
          pulvinar. Vivamus non vehicula metus, eget interdum nunc. Sed bibendum
          leo ac commodo venenatis. Ut malesuada, justo a convallis malesuada,
          metus justo malesuada justo, id cursus neque purus at nisl. Aliquam
          rhoncus tortor dui, vel mattis ex pellentesque eu. Donec volutpat
          vestibulum velit, ut egestas orci molestie a. Vestibulum non cursus
          leo. In hac habitasse platea dictumst. Ut blandit erat purus, sed
          malesuada ligula vehicula ut. Class aptent taciti sociosqu ad litora
          torquent per conubia nostra, per inceptos himenaeos.
        </h5>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { useRouter } from "vue-router";

export default defineComponent({
  setup() {
    const router = useRouter();
    const scrollBack = ref(() => {
      router.push("/");
    });

    return {
      scrollBack,
    };
  },
});
</script>

<style lang="scss" scoped>
@import url("https://fonts.googleapis.com/css2?family=Tajawal:wght@700&display=swap");
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css");

$primary-color: #282828;
$secondary-color: #ea4b4b;

#read-more {
  height: 100%;
  background: url("../../img/intro-background.svg");
  background-position: 50% 30%;
  margin-top: -84px;
  padding-top: 80px;
}

#read-more-content {
  color: $primary-color;
  margin: 35px;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
  }
}

.back-button {
  color: $primary-color;
  background-color: unset;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
  font-size: 0.7rem;
  width: 130px;
  text-align: left;
  padding: 0;
  display: block;
  margin: 0px;
}

.back-button:hover {
  color: $secondary-color;
}
</style>
