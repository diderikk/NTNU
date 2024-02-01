import pygame
import numpy as np
import random
from time import sleep

def drawGrid(screen, color, goal, user, q_table):
    blockSize = 60
    use_color = color
    scalar = 2.5 if np.max(q_table) < 2 else 1
    for x in range(0, 600, blockSize):
        for y in range(0, 600, blockSize):
            use_color = color
            
            rect = pygame.Rect(x, y, blockSize, blockSize)

            q_val = (np.max(q_table[(int(x/60), int(y/60))]) + 1)*scalar
            if q_val > 0:
                temp = np.array(use_color)
                temp[0] = 255
                temp[1] = 255 - int(25*q_val)
                temp[2] = 0
                pygame.draw.rect(screen, tuple(temp), rect)

            if (x, y) == tuple(np.multiply(goal,60)):
                pygame.draw.rect(screen, (0, 255, 0), rect, 60, 60)
            if (x, y) == tuple(np.multiply(user,60)):
                pygame.draw.rect(screen, (0, 0, 255), rect, 60, 60)

            pygame.draw.rect(screen, use_color, rect, 1)


class Environment:
    def __init__(self):
        self.start_pos = np.random.randint(5,9,size=2)
        self.rewards = np.zeros((10,10))
        self.goal = np.array([0, 0])
        self.reset()
        self.calculate_rewards()

  
    def reset(self):
        self.player = np.array(self.start_pos)
        return tuple(self.player)

    def calculate_rewards(self):
        for x in range(len(self.rewards)):
            for y in range(len(self.rewards[0])):
                self.rewards[x, y] = 1 - self.manhatten_distance([x, y])**0.4
    
    def random_action(self):
        random.randint(0,4)

    

    def step(self, action):
        # 0: UP, 1: RIGHT, 2: DOWN, 3: LEFT
        done = tuple(self.player) == tuple(self.goal)
        reward = -1
        if (self.valid_move(action)):
            self.make_new_state(action)
            reward = self.rewards[tuple(self.player)]

            

        return tuple(self.player), reward, done

    def manhatten_distance(self, node):
        return abs(self.goal[0] - node[0]) + abs(self.goal[1] - node[1])
    
    def make_new_state(self, action):
        # 0: UP, 1: RIGHT, 2: DOWN, 3: LEFT
        if(action == 0): self.player[1] -= 1
        elif(action == 1): self.player[0] += 1
        elif(action == 2): self.player[1] += 1
        elif(action == 3): self.player[0] -= 1

    def valid_move(self, action):
        if(action == 0): return self.player[1] > 0
        elif(action == 1): return self.player[0] < 9
        elif(action == 2): return self.player[1] < 9
        elif(action == 3): return self.player[0] > 0
        return False
    
    def render(self, q_table):
        pygame.init()

        screen = pygame.display.set_mode([600, 600])
        pygame.display.set_caption("Gridworld")

        black = (0, 0, 0)
        white = (255, 255, 255)

        done = False

        while not done:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:  # If user clicked close
                    done = True

            state = self.reset()

            done1  = False
            while not done1:
                action = np.argmax(q_table[state])  
                state, _, done1 = self.step(action)
                sleep(0.1)
                screen.fill(black)
                drawGrid(screen, white, self.goal, self.player, q_table)
                pygame.display.flip()
            

        pygame.quit()
    
    def render_train(self):
        pygame.init()

        q_table = np.random.uniform(low=-1, high=1, size=([len(self.rewards)] * 2 + [4]))
        lr = 0.1
        gamma = 0.95
        epsilon = 0.2

        screen = pygame.display.set_mode([600, 600])
        pygame.display.set_caption("Gridworld")

        black = (0, 0, 0)
        white = (255, 255, 255)

        done = False

        while not done:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:  # If user clicked close
                    done = True

            state = self.reset()

            done1  = False
            while not done1:
                if(random.uniform(0, 1) < epsilon):
                    action = self.random_action()
                else:
                    action = np.argmax(q_table[state])

                next_state, reward, done1 = self.step(action)

                old_value = q_table[state + (action, )]
                next_max = np.max(q_table[next_state])
                new_value = (1-lr)*old_value + lr * (reward + gamma * next_max )

                q_table[state + (action, )] = new_value
                state = next_state
                sleep(0.001)
                screen.fill(black)
                drawGrid(screen, white, self.goal, self.player, q_table)
                pygame.display.flip()
            

        pygame.quit()