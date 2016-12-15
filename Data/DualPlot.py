import mlpyPCA
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl

all_weights = np.loadtxt('AllWeights.csv', delimiter=',')
aw = all_weights[:, :10]

pca = mlpyPCA.PCA() # new PCA instance
pca.learn(aw) # learn from data
all_pca = pca.transform(aw, k=2) # embed aw into the k=2 dimensional subspace

x = all_pca[:, 0]
y = all_pca[:, 1]

fig, ax = plt.subplots()
im = ax.hexbin(x, y, gridsize=30)
fig.colorbar(im, ax=ax)

# fig, ax = plt.subplots()
# H = ax.hist2d(x, y, bins=30)
# fig.colorbar(H[3], ax=ax)

plt.ylim([-1.5,0.5])
plt.xlim([-1.5,1.8])

bmu_weights = np.loadtxt('BmuWeights.csv', delimiter=',')
# bw = all_weights[:, :10]
bw_x, bw_y = bmu_weights[:, :11], bmu_weights[:, 11].astype(np.int) # x: (observations x attributes) matrix, y: classes (1: b011212563001, 2: 100, 3: 21670, 4: 23355, 5: admin)

pca = mlpyPCA.PCA() # new PCA instance
pca.learn(bw_x) # learn from data
bmu_pca = pca.transform(bw_x, k=2) # embed x into the k=2 dimensional subspace

colors = ['#660000', '#336600', '#FF00FF', '#3333FF', '#FF0000'] #we'll be cycling through these colors
plt.scatter(bmu_pca[:, 0], bmu_pca[:, 1], color=np.array(colors)[bw_y-1])

# plt.ylim([-1.0,0.5])
# plt.xlim([-1.4,1.5])

plt.ylim([-0.87,0.5])
plt.xlim([-1.28,1.5])

plt.show()


