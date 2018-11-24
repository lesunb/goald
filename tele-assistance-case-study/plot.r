library(ggplot2)

setwd("result")

dataframe <- read.csv(file="tsa_dataset1543065967324" , header=TRUE, sep= "\t", row.names=NULL)

g2 <- ggplot() +
  geom_segment(data=dataframe, aes(x=as.numeric(start), xend=as.numeric(end), y=label, yend=label, colour=color), linetype=1, size=2) +
  
  scale_colour_identity() +
  xlab("Time(ms)")+
  ylab("")+
  theme_bw() + theme(panel.grid.minor = element_blank(), panel.grid.major =   element_blank()) + theme(aspect.ratio = .8)
g2 + theme(legend.position="none")


scale_colour_brewer(palette = "Pastel1")+

avail = aggregate( (end-start) ~ label, data = dataframe, sum)
names(avail)[2]<-"total"
systemAvail = avail$total/12000;
systemAvail
