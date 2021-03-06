#!/usr/bin/rake -f $0 

class String
  def /(b)
    File.join(self, b)
  end
end

class Builder

  def initialize
    @proj_dir  = `pwd`.chomp
    @build_dir = @proj_dir / "build"
    @lib_dirs = [ @proj_dir / "lib" ]
    if `uname -m`.chomp == 'x86_64' then
      @lib_dirs << (@proj_dir / "lib64")
    else
      @lib_dirs << (@proj_dir / "lib32")
    end

    @mahout_tarball = @build_dir / "mahout-distribution-0.7.tar.gz"
    @mahout_src_tarball = @build_dir / "mahout-distribution-0.7-src.tar.gz"
    @remote_mahout_tarball = "http://richcole-test.s3.amazonaws.com/mahout/mahout-distribution-0.7.tar.gz"
    @remote_mahout_src_tarball = "http://richcole-test.s3.amazonaws.com/mahout/mahout-distribution-0.7-src.tar.gz"
    
    @remote_gson_zip = "http://google-gson.googlecode.com/files/google-gson-2.2.2-release.zip"
    @gson_zip = @build_dir / "google-gson-2.2-release.zip" 
    @gson_jar = @build_dir / "google-gson-2.2.2/gson-2.2.2.jar"

    @mahout_dir = @build_dir / "mahout-distribution-0.7"
    @stamps_dir = @build_dir / "stamps"
  end

  def make_stamp(task)
    FileUtils.touch stamp(task) 
  end

  def stamp(task)
    @stamps_dir / task.to_s
  end

  def tasks
 
    task :build_dir do
      FileUtils.mkdir_p @build_dir
    end

    task :stamps do
      FileUtils.mkdir_p @stamps_dir
    end

    task :depends => [:build_dir] do
      if ! File.exist?(@mahout_tarball) then
        sh "wget -O #{@mahout_tarball} #{@remote_mahout_tarball}"
      end
      if ! File.exist?(@mahout_src_tarball) then
        sh "wget -O #{@mahout_src_tarball} #{@remote_mahout_src_tarball}"
      end
      if ! File.exist?(@mahout_dir) then
        sh "tar -xzf #{@mahout_tarball} -C #{@build_dir}"
      end
      if ! File.exist?(@mahout_dir / "pom.xml") then
        sh "tar -xzf #{@mahout_src_tarball} -C #{@build_dir}"
      end
      if ! File.exist?(@gson_zip) then
        sh "wget -O #{@gson_zip} #{@remote_gson_zip}"
      end
      if ! File.exist?(@gson_jar) then
        sh "mkdir -p #{@build_dir}"
	sh "cd #{@build_dir} && unzip #{@gson_zip}"
      end
    end

    task :create_classpath => [:depends] do
      create_classpath
    end

    task :default => [:create_classpath] 
  end

  def create_classpath

    File.open(".classpath", "w") do |outp|
      outp.puts '<?xml version="1.0" encoding="UTF-8"?>'
      outp.puts '<classpath>'
      outp.puts '  <classpathentry kind="src" path="src"/>'
      outp.puts '  <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.6"/>'
      Dir.glob(@mahout_dir / "**" / "*.jar") do |path|
        outp.puts "  <classpathentry kind=\"lib\" path=\"#{clean_path(path)}\"/>"
      end

      for lib_dir in @lib_dirs do
        Dir.glob(lib_dir / "*.jar") do |path|
          outp.puts "  <classpathentry kind=\"lib\" path=\"#{clean_path(path)}\"/>"
        end
      end
      outp.puts "  <classpathentry kind=\"lib\" path=\"#{clean_path(@gson_jar)}\"/>"

      Dir.glob(@mahout_dir / "**" / "java") do |path|
        next if path =~ /\/distribution/
        outp.puts "  <classpathentry kind=\"src\" path=\"#{clean_path(path)}\"/>"
      end
      outp.puts '  <classpathentry kind="output" path="eclipse-bin"/>'
      outp.puts '</classpath>'
    end

  end

  def clean_path(path)
    path.sub(@proj_dir + "/", "")
  end

end

Builder.new.tasks

