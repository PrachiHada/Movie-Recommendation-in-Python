#Reading File in data frame MyData
MyData <- read.csv(file="D:\\SJSU_courses\\256_Large_Scale_Analytics\\40 marks assignment\\PartC\\movies.csv", header=TRUE, sep=",")
str(MyData)

#Creating new data frame which is similar to MyData and adding new column to it
MyData_new <- MyData
MyData_new$avg_rating <- NA
str(MyData_new)

#using library
library(dplyr)
#Finding mean of data
MyData1 <- MyData %>%
  group_by(userid) %>%
  summarise(avg_rating=mean(rating), n=n())
str(MyData1)

#to merge MyData_new and MyData1
MergedData <- merge(MyData_new[, c("userId", "movieId", "rating")], MyData1, by="userid")
str(MergedData)

#to calculate mean centered rating
MergedData <- MergedData %>%
  mutate (norm_rating = rating - avg_rating)

str(MergedData)

#exporting this file
write.csv(MergedData, file = "D:\\SJSU_courses\\256_Large_Scale_Analytics\\40 marks assignment\\movies_norm_test.csv")

#To drop columns rating, avg_rating, n from MergedData
NormData <- subset(MergedData, select = -rating)
NormData <- subset(NormData, select = -avg_rating)
NormData <- subset(NormData, select = -n)

str(NormData)

#exporting this file
write.csv(NormData, file = "D:\\SJSU_courses\\256_Large_Scale_Analytics\\40 marks assignment\\movies_night_norm.csv")


#To convert data into sparse matrix taking 
jaccard_data <- MyData %>%
  mutate(new_rating = ifelse(rating>3, 1, 0))
jaccard_data
#To drop 3rd column 
jaccard_data <- subset(jaccard_data, select = -rating)
jaccard_data

test <- MyData %>%
  summarise(avg_rating=mean(rating), n=n())
str(test)
test$newcol <- NA
