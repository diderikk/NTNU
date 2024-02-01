<template>
  <div id="activity-feed">
     <div id="nav">
    <button @click="goBack" class="back-button">
      <i class="fa fa-arrow-left" aria-hidden="true"></i>
      Gå tilbake
    </button>
    </div>
    <div id="header-title">
      <h2>Mine aktiviteter</h2>
    </div>
    <div class="topnav">
      <button
        class="nav-button"
        id="participant-button"
        @click="toParicipant"
        :disabled="disableButtons"
      >
        Deltaker
      </button>
      <button
        class="nav-button"
        id="organizer-button"
        @click="toOrganizer"
        :disabled="!disableButtons"
      >
        Arrangør
      </button>
    </div>
    <div id="participant" v-if="stage === 1">
      <div class="activities">
        <ActivityFeedItem
          v-for="activity in activitiesParticipant"
          :key="activity.activityId"
          :activityData="activity"
        />
      </div>
    </div>
    <div id="organizer" v-if="stage === 2">
      <div class="activities">
        <li v-for="activity in activitiesOrganizer" :key="activity.activityId">
          <ActivityFeedItem :activityData="activity" />
        </li>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  onBeforeMount,
  ref,
  Ref,
  watchEffect,
} from "vue";
import ActivityFeedItem from "../components/ActivityFeedItem.vue";
import axios from "@/axiosConfig";
import { useRouter } from "vue-router";
import IActivity from "@/interfaces/Activity/IActivity.interface";
import { store } from "../store";

export default defineComponent({
  name: "ActivityFeed",
  components: {
    ActivityFeedItem,
  },
  setup() {
    const router = useRouter();
    const stage = ref(1);
    const activitiesParticipant: Ref<IActivity[]> = ref([]);
    const activitiesOrganizer: Ref<IActivity[]> = ref([]);
    const userId: number = store.getters.user.userId;

    onBeforeMount(async () => {
      try {
        const participantResponse = await axios.get(
          `/users/${userId}/my-activities`
        );
        activitiesParticipant.value = participantResponse.data as IActivity[];
        const organizerResponse = await axios.get(
          `/users/${userId}/organized-activities`
        );
        activitiesOrganizer.value = organizerResponse.data as IActivity[];
      } catch (error) {
        router.push("/error");
      }
    });

    const toParicipant = (): void => {
      stage.value = 1;
    };

    const toOrganizer = (): void => {
      stage.value = 2;
    };

    const disableButtons = computed(() => {
      return stage.value === 1;
    });

     /**
     * Routes user to the previous page
     */
    const goBack = () => {
      router.back();
    };

    return {
      toParicipant,
      toOrganizer,
      stage,
      disableButtons,
      activitiesParticipant,
      activitiesOrganizer,
      goBack,
    };
  },
});
</script>

<style scoped lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Mulish&display=swap");

$primary-color: #282828;
$secondary-color: #ea4b4b;
$padding: 0.6rem 1rem 0.6rem 1rem;

#activity-feed {
  color: $primary-color;
  margin: 35px;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
  }
}

#header-title {
  grid-column: 1/3;
  @media only screen and (min-width: 600px) {
    grid-column: 2/4;
  }
}

h2 {
  font-weight: 600;
  text-align: left;
  margin: 0px;
  @media only screen and (min-width: 600px) {
    text-align: center;
  }
}

h3 {
  margin: 30px 0 30px 0;
}

.topnav {
  margin-top: 20px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  column-gap: 15px;
}

.nav-button {
  border-radius: 0 !important;
  background-color: #ffffff;
  color: $primary-color;
  box-shadow: inset 0 -3px 0 $secondary-color;
}

.nav-button:disabled {
  background-color: $secondary-color;
  color: #ffffff;
}

.activities {
  display: flex;
  flex-flow: wrap;
  text-align: center;
}

#activity {
  margin: 0 auto;
  @media only screen and (min-width: 800px) {
    padding: 10px;
  }
}

li {
  list-style-type: none;
  padding-bottom: 20px;
  @media only screen and (min-width: 800px) {
    width: 50%;
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
  padding: 10px 0px 10px 0px;
  display: block;
}

.back-button:hover {
  color: $secondary-color;
}
</style>
