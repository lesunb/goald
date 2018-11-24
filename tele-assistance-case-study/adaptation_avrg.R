
#install.packages("dplyr")
# library(ggplot2)
library(dplyr)

dataframe <- read.csv(file="tsa_dataset1543038419001" ,head=TRUE,sep="\t")

## group
initial_deployment <- tbl_df(dataframe) > 
  group_by(scenario) >
  summarise(init_depl_avg_time = mean((dam_updated + deployment_change_planned)/1000000),  init_depl_std=sd((dam_updated + deployment_change_planned)/1000000),
            update_1_avg_time = mean((dam_updated.1 + deployment_change_planned.1)/1000000),  update_1_std=sd((dam_updated.1 + deployment_change_planned.1)/1000000),
            update_2_avg_time = mean((dam_updated.2 + deployment_change_planned.2)/1000000),  update_2_std=sd((dam_updated.2 + deployment_change_planned.2)/1000000),
            update_3_avg_time = mean((dam_updated.3 + deployment_change_planned.3)/1000000),  update_3_std=sd((dam_updated.3 + deployment_change_planned.3)/1000000),
            update_4_avg_time = mean((dam_updated.4 + deployment_change_planned.4)/1000000),  update_4_std=sd((dam_updated.4 + deployment_change_planned.4)/1000000)
  )
