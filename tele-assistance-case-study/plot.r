library(ggplot2)
library(dplyr)

setwd("result")

dataframe <- read.csv(file="tsa_dataset1551293710806" , header=TRUE, sep= "\t", row.names=NULL)

#dataframe <-dataframe[which(dataframe$label!="!battery-is-low"),]
#dataframe <-dataframe[which(dataframe$label!="!patient-is-ok"),]

dataframe <- dataframe %>% filter(label!="!patient-is-ok")
dataframe <- dataframe %>% filter(label!="!battery-is-low")

dataframe$color[dataframe$type=="context"] <- "blue"
dataframe$color[dataframe$type=="bundle"] <- "black"
dataframe$color[dataframe$type=="system"] <- "green"
dataframe$color[dataframe$type=="failure"] <- "red"

dataframe$label[dataframe$type=="failure"] <- "system_available"
dataframe$name <- dataframe$label

dataframe$name[dataframe$label=="PushButton-impl"] <- "PushButton"
dataframe$name[dataframe$label=="ProvideSelfDiagnosedEmergenciesSupport-impl"] <-  "ProvideSelfDiagnosedEmergenciesSupport"
dataframe$name[dataframe$label=="ProvideHalthSupport-impl"] <- "ProvideHalthSupport"
dataframe$name[dataframe$label=="AlarmService-impl"] <- "AlarmService"
dataframe$name[dataframe$label=="GetSensedData-impl"] <- "GetSensedData"
dataframe$name[dataframe$label=="MonitorPatient-impl"] <- "MonitorPatient"
dataframe$name[dataframe$label=="RemoteAnalysis-impl"] <- "RemoteAnalysis"
dataframe$name[dataframe$label=="ProvideAutomatedLifeSupport-impl"] <- "ProvideAutomatedLifeSupport"
dataframe$name[dataframe$label=="SendSMS-impl"] <- "SendSMS"
dataframe$name[dataframe$label=="LocalAnalysis-impl"] <- "LocalAnalysis"
dataframe$name[dataframe$label=="RemoteAnalysis-impl"] <- "RemoteAnalysis"
dataframe$name[dataframe$label=="AlarmService-impl"] <- "AlarmService"
dataframe$name[dataframe$label=="EnactTreatment-impl"] <- "EnactTreatment"
dataframe$name[dataframe$label=="AdministerMedicine-impl"] <- "AdministerMedicine"
dataframe$name[dataframe$label=="ChangeDrug-impl"] <- "ChangeDrug"
dataframe$name[dataframe$label=="ChangeDose-impl"] <- "ChangeDose"
dataframe$name[dataframe$label=="SendSMS-impl"] <- "SendSMS"
dataframe$name[dataframe$label=="LocalAnalysis-impl"] <- "LocalAnalysis"



dataframe$start <- dataframe$start/(60*60*1000)
dataframe$end <- dataframe$end/(60*60*1000)

dataframe <-dataframe[order(-1*dataframe$plotIndex),]

dataframe$label <- factor(dataframe$label, levels=unique(dataframe$label))
avail = aggregate( (end-start) ~ label, data = dataframe, sum)
dataframe<-merge(dataframe, avail, by="label", all.x=TRUE)

fds = dataframe[dataframe$type == 'failure' ,]
fds = fds[fds$start !=0,] #remove unavailable at start

dataframe$label[dataframe$type=="failure"] <- "system_available"

g2 <- ggplot() +
  #context, bundles and system
  geom_segment(data=dataframe, aes(x=as.numeric(start), xend=as.numeric(end),  y=label, yend=label, colour=color), linetype=1, size=2) +
  #failures
  geom_segment(data=fds, aes(x=as.numeric(start)+0.07, xend=as.numeric(end),  y="battery-is-low", yend="system_available", colour=color), linetype=3, size=0.7) +
  #failures
  geom_segment(data=fds, aes(x=as.numeric(start-0.035), xend=as.numeric((end+0.035)),  y="system_available", yend="system_available", colour=color), linetype=1, size=2) +
  scale_colour_identity() +
  scale_x_discrete(limit = c(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20),
                   labels=c(0,"","",3,"","",6,"","",9,"","",12,"","",15,"","",18,"","")) +
  xlab("Time ( hours )")+
  ylab("")+
  
  theme_bw() + theme(panel.grid.minor = element_blank(), panel.grid.major =   element_blank()) + theme(aspect.ratio = .8)
g2 + theme(legend.position="none")


avail = aggregate( (end-start) ~ label, data = dataframe, sum)
names(avail)[2]<-"total"
systemAvail = avail$total/12000;
