# SetCover
CSE373 HW4

Here are running time of some of test cases:
S-k-20-30: 1s
S-k-20-35: 1s
S-k-30-50: 1s
S-k-40-60: 1s
S-k-35-65: 3s
S-k-40-80: 21s
S-k-50-95: unknown

s-X-12-6: 1s
S-rg-8-10: 1s
S-rg-31-15: 1s
S-rg-40-20: 1s
S-rg-63-25: 1s
S-rg-118-30: 52s
S-rg-109-35: unknown 

Here are how I implement my program:

First of all, I traverse all the elements in all the subsets to see if there are unique element that appear only once in all the subsets. This means that subsets that contains the unique element is always needed in my answer. However I notice that, this does help much because almost all of elements have duplicate.

Also, before I move into the backtracking stage, I sort the all the subsets based the number of elements in each subset. So the biggest subset is at the front and the smallest subset at the end. The advantage for this optimization is that those bigger subset contains more elements and thus have a higher probability to be in the result and help the program find the result earlier.

A big achievement I did is making the complexity of isValid() method O(1). By having an array whose length is the length +1. The first element of the array is the number of non-Zero entries in the array. Then each element of array is the number of each element of universe in the partial solution. I revise this array every time I add a subset into partial solution and remove. So in isValid method, it just check whether the first element of the array is 0 or not.

The array representing the number of every element in current partial solution mentioned above has another purpose in constructCandidates() method. In this method, I have to choose candidates among the rest of subsets that is not in the partial solution. By checking whether every element of that possible subset is already coved in the partial solution, I can say whether a subset is not needed and I will not put that into candidates. By also I can prune a lot of  unnecessary branches. 

For test cases whose name start with “s-k”, from my observation, many of subsets have only one elements, and that elements already appear in another subset. That mean, those subsets could be discarded with going into the backtracking. This makes the problem much less complex. For instance, in the test “s-k-40-80”, and half of all the subsets have only one elements and already covered in other subsets. So I can delete those subsets. Then there are only 40 subsets left to move into the backtracking stage.

When the partial solution at a certain stage have 6 subsets and the temporary result I saved also has size of 6, I intentionally avoid it to backtrack to make the partial solution of size 7. No matter whether we can get a result from that partial solution, the length of that is always bigger or equal to the result. It doesn’t help us with the minimum size of set cover. By pruning many unnecessary branches, the time the program took significantly dropped.
