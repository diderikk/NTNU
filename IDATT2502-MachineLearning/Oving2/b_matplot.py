import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import torch

x_train = torch.tensor([[0,0], [1,0], [0,1], [1,1]], dtype=torch.float)
y_train = torch.tensor([[1], [1], [1], [0]], dtype=torch.float)

class NandOperatorNN:
	def __init__(self):
		self.W = torch.tensor([[1.0], [3.0]], requires_grad=True)
		self.b = torch.tensor([2.5], requires_grad=True)
	
	def logits(self, x):
		return x @ self.W + self.b
	
	def f(self, x):
		return torch.sigmoid(x @ self.W + self.b)

	def loss(self, x, y):
		return torch.nn.functional.binary_cross_entropy_with_logits(self.logits(x), y)
		# return -torch.mean(torch.multiply(y, torch.log(self.f(x))) + torch.multiply((1-y), torch.log(1 - self.f(x))))

model = NandOperatorNN()
optimizer = torch.optim.SGD([model.W, model.b], lr=10)

for epoch in range(10000):
	model.loss(x_train, y_train).backward()
	optimizer.step()
	optimizer.zero_grad()

print(f'W = {model.W}, b = {model.b}, loss = {model.loss(x_train, y_train)}')

fig = plt.figure(figsize=(10,10))
ax = fig.add_subplot(111, projection='3d')
ax.set_xlabel("$x_1$")
ax.set_ylabel("$x_2$")
ax.set_zlabel("$y$")
ax.set_zlim(0, 1.25)

x1, x2 = torch.meshgrid(torch.linspace(0, 1.25, 100), torch.linspace(0, 1.25, 100))

y_grid = torch.empty([100,100])

for i in range(100):
	for j in range(100):
		x = torch.tensor([[x1[i,j], x2[i,j]]])
		y_grid[i,j] = model.f(x).item()

# x = torch.column_stack((x1, x2))
print(y_grid)
ax.plot_wireframe(x1,x2,y_grid, color='red')

plt.show()


