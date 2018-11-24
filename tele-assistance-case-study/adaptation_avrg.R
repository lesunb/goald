install.packages("dplyr")
# library(ggplot2)
library(dplyr)

dataframe <- read.csv(file="fsa_dataset" ,head=TRUE,sep="\t")

## group
initial_deployment <- tbl_df(dataframe) %>% 
  group_by(scenario) %>%
  summarise(init_depl_avg_time = mean((dam_updated + deployment_change_planned)/1000000),  init_depl_std=sd((dam_updated + deployment_change_planned)/1000000),
            update_1_avg_time = mean((dam_updated.1 + deployment_change_planned.1)/1000000),  update_1_std=sd((dam_updated.1 + deployment_change_planned.1)/1000000),
            update_2_avg_time = mean((dam_updated.2 + deployment_change_planned.2)/1000000),  update_2_std=sd((dam_updated.2 + deployment_change_planned.2)/1000000),
            update_3_avg_time = mean((dam_updated.3 + deployment_change_planned.3)/1000000),  update_3_std=sd((dam_updated.3 + deployment_change_planned.3)/1000000),
            update_4_avg_time = mean((dam_updated.4 + deployment_change_planned.4)/1000000),  update_4_std=sd((dam_updated.4 + deployment_change_planned.4)/1000000)
  )

pdf("fsa.pdf")
boxplot(init_depl_avg_time~scenario,data=initial_deployment, xlab="Size", ylab="Time(ms)")
dev.off()

initial_deployment
write.table(initial_deployment, "initial_deployment.txt", sep="\t")

update_deployment <- tbl_df(dataframe) %>% 
  group_by(scenario, operation, exec_index) %>%
  filter(row_number() > 1) %>% # only the first, the initial plan
  group_by(scenario, operation ) %>%
  summarise(avg_time = mean(time/1000000),  std=sd(time/1000000))

update_deployment

write.table(update_deployment, "update_deployment.txt", sep="\t")

