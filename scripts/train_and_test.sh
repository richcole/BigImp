;; This buffer is for notes you don't want to save, and for Lisp evaluation.
;; If you want to create a file, visit that file with C-x C-f,
;; then enter the text in that file's own buffer.

export HADOOP_CONF_DIR=/home/hadoop/conf
export PATH=$PATH:/mnt/mahout/mahout-distribution-0.7/bin


i=$(uuidgen); 
echo $i

TRAINING_INPUT_FILE=/mnt/data/iter0/ftrs.txt

hadoop fs -rm hdfs:///$TRAINING_INPUT_FILE
hadoop fs -cp file:///$TRAINING_INPUT_FILE hdfs:///$TRAINING_INPUT_FILE

mahout org.apache.mahout.classifier.df.tools.Describe -p $TRAINING_INPUT_FILE -f $TRAINING_INPUT_FILE.info -d 10 N L

# ignore 1 field, 9 numeric fields, label

OUTPUT_PATH=/tmp/output/$i
mahout org.apache.mahout.classifier.df.mapreduce.BuildForest -h -ds $TRAINING_INPUT_FILE.info -d $TRAINING_INPUT_FILE --nbtrees 5 --output $OUTPUT_PATH --partial

mahout org.apache.mahout.classifier.df.mapreduce.TestForest -i $TRAINING_INPUT_FILE -ds $TRAINING_INPUT_FILE.info -m $OUTPUT_PATH -a -mr -o $OUTPUT_PATH/predictions
