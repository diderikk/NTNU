<template>
  <div id="absence-registration">
     <div id="nav">
    <button @click="goBack" class="back-button">
      <i class="fa fa-arrow-left" aria-hidden="true"></i>
      Gå tilbake
    </button>
    </div>

    <h2>Registrer fravær</h2>
    <h4>
      Kryss av for alle som ikke møtte opp til aktiviteten, og trykk på
      registrer knappen når du er ferdig
    </h4>
    <div v-for="(participant, index) in participants" :key="index">
      <input type="checkbox" v-model="participant.absent" />
      {{ participant.participant.forename }}
      {{ participant.participant.surname }}
    </div>
    <button @click="register">Registrer</button>
    <button v-if="selectAllAvailable" @click="setAbsentValuesForAll(true)">
      Velg alle
    </button>
    <button v-else @click="setAbsentValuesForAll(false)">Fjern alle</button>
  </div>
</template>

<script lang="ts">
import { defineComponent, onBeforeMount, Ref, ref } from "vue";
import { useRouter } from "vue-router";
import axios from "../axiosConfig";
import User from "../interfaces/User/User.interface";
import { useStore } from "../store";
export default defineComponent({
  name: "ActivityRegisterAbsence",
  props: {
    id: {
      required: true,
      type: String,
    },
  },
  setup(props) {
    const participants: Ref<{ participant: User; absent: boolean }[]> = ref([]);
    const absences: Ref<Array<number>> = ref([]);
    const store = useStore();

    onBeforeMount(async () => {
      try {
        const response = await axios.get(`/activities/${props.id}/users`);
        const temporaryParticipants = response.data;
        temporaryParticipants.forEach((participant: User) => {
          if (participant.userId !== store.getters.user.userId) {
            participants.value.push({
              participant: participant,
              absent: false,
            });
          }
        });
      } catch (error) {
        alert("Cannot load activity");
        router.back();
      }
    });

    const selectAllAvailable = ref(true);

    const router = useRouter();

    /**
     * Set absent value for all participangs equal to the boolean param value
     */
    const setAbsentValuesForAll = (value: boolean) => {
      selectAllAvailable.value = !selectAllAvailable.value;
      participants.value.forEach((participant) => {
        participant.absent = value;
      });
    };

    /**
     * Used to register the absence of users for an activity, the selcted amount of users has to be above
     */
    const register = async () => {
      try {
        absences.value = [];
        participants.value.forEach((participant) => {
          if (participant.absent) {
            absences.value.push(participant.participant.userId);
          }
        });
        if (absences.value.length > 0) {
          await axios.post(`/activities/${props.id}/absences`, {
            absentUsersId: absences.value,
          });
          router.push(`/activity/${props.id}`);
        }
      } catch (error) {
        router.push("/error");
      }
    };

    /**
     * Makes user return to previous page
     */
    const goBack = (): void => {
      router.back();
    };

    return {
      participants,
      goBack,
      setAbsentValuesForAll,
      register,
      selectAllAvailable,
    };
  },
});
</script>

<style scoped lang="scss">

$primary-color: #282828;
$secondary-color: #ea4b4b;
$padding: 0.6rem 1rem 0.6rem 1rem;

#absence-registration {
    margin: 35px;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
    padding: 20px;
  }
}

button {
  margin: $padding;
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
